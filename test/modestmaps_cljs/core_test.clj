(ns modestmaps-cljs.core-test
  (:require
   [clojure.test :refer :all]
   [modestmaps-cljs.core :refer :all]))


(deftest test-point
  (let [p #modestmaps_cljs.core.Point{:x 10, :y 10}]
    (testing "#Point{:x 10 :y 10}"
      (is (= (:x p) 10)))))


(deftest test-coordinate
  (testing "#Coordinate{:row 10 :column 10 :zoom 1}"
    (testing "(container coordinate)")
    (testing "(zoom-to coordinate)")
    (testing "(zoom-by coordinate")
    (testing "(up coordinate)")
    (testing "(right coordinate")
    (testing "(down coordinate")
    (testing "(left coordinate)")))
