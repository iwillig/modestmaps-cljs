(ns modestmaps-cljs.core
  (:import [java.lang Math]))

(def max-zoom 25)

(defrecord Location [lat lon])

(defprotocol IExtent
  (get-north-west        [self])
  (get-south-east        [self])
  (get-north-east        [self])
  (get-south-west        [self])
  (get-center            [self])
  (enclose-location  [self])
  (from-locations    [self locations])
  (enclose-extent    [self extent])
  (contains-location [self location]))


(defrecord Extent [north west south east]
  IExtent
  (get-north-west [self]
    (->Location north west))
  (get-south-east [self]
    (->Location south east))
  (get-north-east [self]
    (->Location north east))
  (get-south-west [self]
    (->Location south west))
  (get-center [self]
    (->Location
     (+ south (* (- north south) 0.5))
     (+ east (* (- west east) 0.5)))))

(defn make-extent [north west south east]
  (->Extent (Math/max north south)
            (Math/min north south)
            (Math/max east west)
            (Math/min east west)))

(defprotocol IPoint
  (distance    [self other])
  (interpolate [self other]))

(defrecord Point [x y]
  IPoint
  (distance    [self other])
  (interpolate [self other]))

(defprotocol ICoordinate
  (container [self])
  (zoom-to   [self destination])
  (zoom-by   [self distance])
  (up        [self distance])
  (right     [self distance])
  (down      [self distance])
  (left      [self distance]))


(defrecord Coordinate [row column zoom]
  ICoordinate

  (container [self]
    (->Coordinate
     (Math/floor row)
     (Math/floor column)
     (Math/floor zoom)))

  (zoom-to   [self destination]
    (->Coordinate
     (* row (Math/pow 2 (- destination zoom)))
     (* column (Math/pow 2 (- destination zoom)))
     destination))

  (zoom-by   [self distance]
    (->Coordinate
     (* row (Math/pow 2 distance))
     (* column (Math/pow 2 distance))
     (+ zoom distance)))

  (up        [self distance]
    (->Coordinate
     (- row distance)
     column
     zoom))

  (right     [self distance]
    (->Coordinate
     row
     (+ column distance)
     zoom))

  (down      [self distance]
    (->Coordinate
     (+ row distance)
     column
     zoom))

  (left      [self distance]
    (->Coordinate
     row
     (- column distance)
     zoom)))

