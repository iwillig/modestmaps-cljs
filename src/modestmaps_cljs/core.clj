(ns modestmaps-cljs.core
  (:import [java.lang Math]))

(def max-zoom 25)

(defrecord Point [x y])

(defprotocol ICoordinate
  (container [self])
  (zoom-to   [self destination])
  (zoom-by   [self distance])
  (up        [self distance])
  (right     [self distance])
  (down      [self distance])
  (left      [self distance]))


(defrecord Coordinate [col row zoom]
  ICoordinate

  (container [self]
    (->Coordinate
     (Math/floor (:row self))
     (Math/floor (:column self))
     (:zoom self)))

  ;; self.row * math.pow(2, destination - self.zoom,
  (zoom-to   [self destination]
    (->Coordinate
     (* (:row self) (Math/pow 2 (:zoom self)))
     (* (:column self) (Math/pow 2 destination))
     (+ (:zoom self) destination)))

  (zoom-by   [self distance]
    (->Coordinate
     (* (:row self) (Math/pow 2 distance))
     (* (:column self) (Math/pow 2 distance))
     (+ (:zoom self) distance)))

  (up        [self distance]
    (->Coordinate
     (- (:row self) distance)
     (:column self)
     (:zoom self)))

  (right     [self distance]
    (->Coordinate
     (:row self)
     (+ (:column self) distance)
     (:zoom self)))

  (down      [self distance]
    (->Coordinate
     (+ (:row self) distance)
     (:column self)
     (:zoom self)))

  (left      [self distance]
    (->Coordinate
     (:row self)
     (- (:column self) distance)
     (:zoom self))))

