(ns modestmaps.core
  (:import [java.lang Math]))

(def max-zoom 25)


(defn format-tile [layer coordinate] chan)
(defn generate-tiles [layer chan])
(defn draw-tile [layer chan])


(defrecord Location [lat lon])

(defprotocol IExtent
  (north-west        [self])
  (south-east        [self])
  (north-east        [self])
  (south-west        [self])
  (center            [self])

  (enclose-location  [self location])
  (enclose-locations [self locations])
  (enclose-extent    [self other])
  (contains-location? [self location]))

(defrecord Extent [north west south east]
  IExtent
  (north-west [self]
    (->Location north west))
  (south-east [self]
    (->Location south east))
  (north-east [self]
    (->Location north east))
  (south-west [self]
    (->Location south west))
  (center [self]
    (->Location
     (+ south (* (- north south) 0.5))
     (+ east (* (- west east) 0.5))))

  (enclose-location [self location]
    (->Extent (Math/max (:lat location) north)
              (Math/min (:lon location) west)
              (Math/min (:lat location) south)
              (Math/max (:lon location) east)))

  (enclose-locations [self locations]
    (reduce #(enclose-location %1 %2) self locations))

  (enclose-extent [self other]
    (->Extent (Math/max north (:north other))
              (Math/min south (:sourth other))
              (Math/max east  (:east other))
              (Math/min west  (:west other))))

  (contains-location? [self location]
    (and (>= (:lat location) south)
         (<= (:lat location) north)
         (>= (:lon location) west)
         (<= (:lon location) east))))


(defn make-extent [north west south east]
  (->Extent (Math/max north south)
            (Math/min north south)
            (Math/max east west)
            (Math/min east west)))

(defprotocol IPoint
  (distance    [self other])
  (interpolate [self other t]))

(defrecord Point [x y]
  IPoint
  (distance    [self other]
    (Math/sqrt (+ (Math/pow (- (:x other) x) 2)
                  (Math/pow (- (:y other) y) 2))))

  (interpolate [self other t]
    (->Point (* (+ x (- (:x other) x)) t)
             (* (+ y (- (:y other) y)) t))))

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

