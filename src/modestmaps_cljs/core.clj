(ns modestmaps-cljs.core)

(defprotocol ICoordinate
  (container [self])
  (zoom-to   [self])
  (zoom-by   [self])
  (up        [self])
  (right     [self])
  (down      [self])
  (left      [self]))


(defrecord Coordinate [row column zoom]
  ICoordinate
  (container [self])
  (zoom-to   [self])
  (zoom-by   [self])
  (up        [self])
  (right     [self])
  (down      [self])
  (left      [self]))
