(ns modestmaps.tile
  (:import  [java.lang Math])
  (:require [modestmaps.core :refer [->Coordinate]]))

(defn n-tiles-in-zoom
  [zoom]
  {:pre  [(>= zoom 0)]}
  (reduce (fn [n i] (+ n (Math/pow 4 i))) 0 (range (+ 1 zoom))))


(defn seed-tiles
  [{:keys [zoom-start zoom-end] :or {zoom-start 0 zoom-end 2}}]
  (for [zoom (range zoom-start (+ 1 zoom-end))
        :let [limit (Math/pow 2 zoom)]]
    (for [col (range limit)]
      (for [row (range limit)]
        (->Coordinate zoom col row)))))

(defn num->deg
  [x y zoom])

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
