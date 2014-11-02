(ns modestmaps.projection-test
  (:import [java.lang Math])
  (:require
   [clojure.test :refer :all]
   [modestmaps.core            :refer [->Location ->Coordinate]]
   [modestmaps.transformation  :refer [->Transformation transform untransform]]
   [modestmaps.projection      :refer :all]))


(let [m (->MercatorProjection 10 (->Transformation 1 0 0 0 1 0))
      l (->LinearProjection   10 (->Transformation 1 0 0 0 1 0))]

  (deftest test-projection

    ;; this is broken
    ;; (is (= (location-coordinate m (->Location 0 0))
    ;;        (->Coordinate -1.1102230246251565E-16 0 10)))

    (testing "linear projection does modify points"
      (let [point     (project l {:x 10 :y 10})
            unpoint   (unproject l {:x 10 :y 10})]
        (is (= (:x point) 10))
        (is (= (:y point) 10))
        (is (= (:x unpoint) 10))
        (is (= (:y unpoint) 10))))

    (testing "location-coordinate"
      (testing "is accurate up to 3 decimals"
        (let [c2 (location-coordinate m (->Location 37 -122))]
          (is (=
               (Math/round (* (:row c2)
                              1000))
               696))
          (is (=
               (Math/round (* (:column c2)
                              1000))
               -2129))
          (is (= (:zoom 10))))))

    (testing "coordinate-location"
      (testing "is accurate up to 3 decimals"
        (let [l2 (coodrinate-location m (->Coordinate 0.696 -2.129 10))]

          (is (=
               (* (:lat l2) 1000)
               3700))


          (is (= (* (:lon l2) 1000)
                 -12198))
          
          )))))
