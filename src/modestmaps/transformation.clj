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


