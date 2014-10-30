(ns modestmaps.transformation
  (:require
   [modestmaps.core :refer [->Point]]))

(defprotocol ITransformation
  (transform   [self point])
  (untransform [self point]))

(defrecord Transformation [ax bx cx ay by cy]
  ITransformation
  (transform   [self point]
    (->Point (+ (* (:ax self) (:x point))
                (* (:bx self) (:y point))
                (:cx self))
             (+ (* (:ay self) (:x point))
                (* (:by self) (:y point))
                (:cy self))))

  (untransform [self point]
    (->Point (/  (+ (- (* (:x point) (:by self))
                       (* (:y point) (:bx self))
                       (* (:cx self) (:by self)))
                    (* (:cy self) (:bx self)))

                 (- (* (:ax self) (:by self))
                    (* (:ay self) (:bx self))))
             (/
              (+ (- (* (:x point) (:ay self))
                    (* (:y point) (:ax self))
                    (* (:cx self) (:ay self)))
                 (* (:cy self) (:ax self)))

              (- (* (:bx self) (:ay self))
                 (* (:by self) (:ax self)))))))

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

