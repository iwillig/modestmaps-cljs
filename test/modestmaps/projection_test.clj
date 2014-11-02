(ns modestmaps.projection-test
  (:import [java.lang Math])
  (:require
   [clojure.test :refer :all]
   [modestmaps.core            :refer [->Location ->Coordinate]]
   [modestmaps.transformation  :refer [->Transformation transform untransform]]
   [modestmaps.projection      :refer :all]))

(def m (->MercatorProjection 10 (->Transformation 1 0 0 0 1 0)))

(defn near [n val]
  (= (Math/round (* n   10000))
     (Math/round (* val 10000))))

(let [m (->MercatorProjection 10 (->Transformation 1 0 0 0 1 0))
      l (->LinearProjection   10 (->Transformation 1 0 0 0 1 0))]

  (deftest test-projection

    (testing "Mercator Projection"
      (let [coord (location-coordinate m (->Location 0 0))]
        (is (near (:row coord) 0.0))
        (is (= (:column coord) 0.0))
        (is (= (:zoom coord) 10))))

    (testing "linear projection does modify points"
      (let [point     (project l {:x 10 :y 10})
            unpoint   (unproject l {:x 10 :y 10})]
        (are [x] (= x 10)
             (:x point)
             (:y point)
             (:x unpoint)
             (:y unpoint))))

    (testing "location-coordinate"
      (testing "is accurate up to 3 decimals"
        (let [c2 (location-coordinate m (->Location 37 -122))]

          (is (= (Math/round (* (:row c2)  1000)) 696))
          (is (= (Math/round (* (:column c2) 1000)) -2129))
          (is (= (:zoom 10))))))

    (testing "coordinate-location"
      (testing "is accurate up to 3 decimals"
        (let [l2 (coordinate-location m (->Coordinate 0.696 -2.129 10))]

          (is (= (Math/round (* (:lat l2) 100)) 3700))
          (is (= (Math/round (* (:lon l2) 100)) -12198))) ))))

