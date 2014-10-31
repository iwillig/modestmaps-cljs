(ns modestmaps.tile
  (:import  [java.lang Math])
  (:require [modestmaps.core :refer [->Coordinate]]))

(defn n-tiles-in-zoom
  [zoom]
  {:pre  [(>= zoom 0)]}
  (reduce (fn [n i] (+ n (Math/pow 4 i))) 0 (range (+ 1 zoom))))

(defn generate-parents [coord])

(defn seed-tiles
  [{:keys [zoom-start zoom-end]
    :or {zoom-start 0 zoom-end 3}}]
  (for [zoom (range zoom-start (+ 1 zoom-end))
        :let [limit (Math/pow 2 zoom)]]
    (for [col (range limit)]
      (for [row (range limit)]
        (->Coordinate zoom col row)))))

;; http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
(defn num->deg
  [xtile ytile zoom]
  (let [n        (Math/pow 2 zoom)
        long-deg (/ xtile (- (* n 360 ) 180))
        lat-rad  (Math/atan (Math/sinh (* Math/PI  (- 1 (/ n (* 2 ytile))))))
        lat-deg  (Math/toDegrees lat-rad)]
    [lat-deg long-deg]))


;; http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
(defn deg->num
  [lat lon zoom]
  (let [lat-rad (Math/toRadians lat)
        n       (Math/pow 2 zoom)
        xtile   (* (/ (+ lat 180)
                      360)
                   n)
;;     int((1.0 - math.log(math.tan(lat_rad) + (1 / math.cos(lat_rad))) / math.pi) / 2.0 * n)
        ytile   (* (/ (/ (- 1 (Math/log (+
                                            (Math/tan lat-rad)
                                            (/ 1 (Math/cos lat-rad)))))
                          Math/PI) 2) n)
        ]
    [xtile ytile]))


(defn coord->bounds
  [coord]
  (let [[top-left-lat, top-left-lng] (num->deg (:column coord)
                                               (:row    coord)
                                               (:zoom   coord))
        [bottom-right-lat bottom-right-lng] (num->deg (+ 1 (:column coord))
                                                      (+ 1 (:row coord))
                                                      (:zoom coord))
        minx top-left-lng
        miny bottom-right-lat
        maxx (Math/min 180 bottom-right-lng)
        maxy (Math/min 90  top-left-lat)]
    [minx miny maxx maxy]))


(defn bounds->coords
  [bounds zoom]
  (let [[minx miny maxx maxy] bounds
        top-left-lng minx
        top-left-lat maxy
        bottom-right-lat miny
        bottom-right-lng maxx
        [top-left-x top-left-y] (deg->num top-left-lat top-left-lng zoom )
        [bottom-right-x bottom-right-y] (deg->num bottom-right-lat bottom-right-lng zoom)
        maxval (- (Math/pow 2 zoom) 1)
        bottom-right-x (Math/min maxval bottom-right-x)
        bottom-right-y (Math/min maxval bottom-right-y)
        top-left-coord (->Coordinate top-left-y top-left-x zoom)]

    (if (and (= top-left-x bottom-right-x)
             (= top-left-y bottom-right-y))
      [top-left-coord]
      [top-left-coord (->Coordinate bottom-right-y
                                    bottom-right-x
                                    zoom)])))

(defn generate-tile-for-single-bounds
  [bounds zoom-start zoom-unitl])

(defn generate-tile-for-range
  [start-col start-row end-col end-row zoom-start zoom-until])
