(ns modestmaps.tile
  (:import  [java.lang Math])
  (:require [modestmaps.core :refer [->Coordinate]]))

(defn n-tiles-in-zoom
  [zoom]
  {:pre  [(>= zoom 0)]}
  (reduce (fn [n i] (+ n (Math/pow 4 i))) 0 (range (+ 1 zoom))))

(defn generate-parents [coord])

(defn seed-tiles
  [{:keys [zoom-start zoom-end] :or {zoom-start 0 zoom-end 3}}]
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
  [lat lon zoom])

(defn coord->bounds
  [coord])

(defn bounds->coords
  [coord])

(defn generate-tile-for-single-bounds
  [bounds zoom-start zoom-unitl])

(defn generate-tile-for-range
  [start-col start-row end-col end-row zoom-start zoom-until])
