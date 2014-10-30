(ns modestmaps.map-test
  (:require
   [clojure.test :refer :all]
   [modestmaps.map  :refer :all]
   [modestmaps.core :refer :all]))



(let [the-map {:parent {}
               :projection {}
               :coordinate {}
               :tile-size {:height 256 :width 256}
               :dimensions {:x 10 :y 10}}]

  (deftest test-map
    (print the-map)
    (is (= false true))))

