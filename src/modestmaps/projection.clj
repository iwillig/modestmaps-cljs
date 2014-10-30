(ns modestmaps.projection
  (:import
   [java.lang Math])
  (:require
   [modestmaps.transformation :refer [transform
                                      untransform
                                      ->Transformation]]
   [modestmaps.core :refer [->Point
                            ->Coordinate
                            zoom-to
                            ->Location]]))

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

