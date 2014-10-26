(ns modestmaps-cljs.core)

(def max-zoom 25)

(defrecord Point [x y])

(defprotocol ICoordinate
  (container [self])
  (zoom-to   [self destination])
  (zoom-by   [self distance])
  (up        [self])
  (right     [self])
  (down      [self])
  (left      [self]))


(defrecord Coordinate [row column zoom]
  ICoordinate

  (container [self]
    (->Coordinate
     (Math.floor (:row self))
     (Math.floor (:column self))
     (:zoom self)))

  ;; self.row * math.pow(2, destination - self.zoom,
  (zoom-to   [self destination]
    (->Coordinate
     (* (:row self) (Math.pow 2 (:zoom self)))
     (* (:column self) (Math.pow 2 distance))
     (+ (:zoom self) distance) ))

  (zoom-by   [self distance]
    (->Coordinate
     (* (:row self) (Math.pow 2 distance))
     (* (:column self) (Math.pow 2 distance))
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

  (left      [self]
    (->Coordinate
     (:row self)
     (- (:column self) distance)
     (:zoom self))))

