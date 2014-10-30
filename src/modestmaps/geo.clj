(ns modestmaps.geo
  (:require
   [modestmaps.transformation :refer [transform untransform ->Transformation]]
   [modestmaps.core :refer [->Point ->Coordinate zoom-to ->Location]])
  (:import
   [java.lang Math]))

(defn linear-solution
  [r1 s1 t1 r2 s2 t2 r3 s3 t3]
  (let [a    (/ (- (* (- t2 t3)
                      (- s1 s2))
                   (* (- t1 t2)
                      (- s2 s3)))
                (- (* (- r2 r3)
                      (- s1 s2))
                   (* (- r1 r2)
                      (- s2 s3))))

        b    (/ (- (* (- t2 t3)
                      (- r1 r2))
                   (* (- t1 t2)
                      (- r2 r3)))
                (- (* (- s2 s3)
                      (- r1 r2))
                   (* (- s1 s2)
                      (- r2 r3))))
        c   (- t1 (* r1 a) (* s1 b))]

    [a b c]))

(defn derive-transformation [a1x a1y a2x a2y b1x b1y b2x b2y c1x c1y c2x c2y]
  (let [[ax bx cx] (linear-solution a1x a1y a2x b1x b1y b2x c1x c1y c2x)
        [ay by cy] (linear-solution a1x a1y a2y b1x b1y b2y c1x c1y c2y)]
    (->Transformation ax bx cx ay by cy)))


(defprotocol IProjection
  (raw-project         [self point])
  (raw-unproject       [self point])
  (project             [self point])
  (unproject           [self point])
  (location-coordinate [self location])
  (coodrinate-location [self coordinate]))


(def abstract-projection-impl
  {:project (fn [self point]
              (let [point (raw-project self point)]
                (transform (:transformation self) point)))

   :unproject (fn [self point]
                (let [point (untransform (:transformation self) point)]
                  (raw-unproject self point)))

   :location-coordinate (fn [self location]
                          (let [point (->Point (/ (* Math/PI (:lon location))
                                                  180.0)
                                               (/ (* Math/PI (:lat location))
                                                  180.0))
                                project-point (project self point)]
                            (->Coordinate (:y project-point)
                                          (:x project-point)
                                          (:zoom self))))

   :coordinate-location (fn [self coordinate]
                          (let [coordinate (zoom-to coordinate  (:zoom self))
                                point      (unproject self (->Point (:column coordinate)
                                                                    (:row coordinate)))]
                            (->Location (/ (* 180.0 (:y point)) Math/PI)
                                        (/ (* 180.0 (:x point)) Math/PI))))})

(defrecord LinearProjection   [zoom transformation])

(extend LinearProjection
  IProjection
  (assoc abstract-projection-impl
    :raw-project   (fn [self point] point)
    :raw-unproject (fn [self point] point)))

(defrecord MercatorProjection [zoom transformation])

(extend MercatorProjection
  IProjection
  (assoc abstract-projection-impl
    :raw-project   (fn [self point]
                     (->Point (:x point)
                              (Math/log (Math/tan (+ (* 0.25 Math/PI)
                                                     (* 0.5  (:y point)))))))
    :raw-unproject (fn [self point]
                     (->Point (:x point)
                              (-
                               (* 2 (Math/atan (Math/pow Math/E (:y point))))
                               (* 0.5 Math/PI))))))
