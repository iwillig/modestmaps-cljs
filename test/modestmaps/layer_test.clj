(ns modestmaps.layer-test
  (:require
   [clojure.test :refer :all]
   [modestmaps.core :refer :all]))

(let [one {:url_template "http://{S}tile.openstreetmap.org/{Z}/{X}/{Y}.png"}]
  (println one))
