(ns modestmaps.geo-test
  (:require
   [clojure.test         :refer :all]
   [modestmaps.core :refer [->Point ->Coordinate ->Location]]
   [modestmaps.transformation :refer [->Transformation transform untransform]]
   [modestmaps.projection :refer :all]))


(deftest test-transformation
  (testing "Transformation"
    (let [t  (->Transformation 1 0 0 0 1 0)
          p  (->Point 1 1)]
      (testing "(transform t p)"
        (is (= (transform t p) (->Point 1 1))))
      (testing "(untransform t p)"
        (is (= (untransform t p) (->Point 1 1)))))

    (let [t (->Transformation 0 1 0 1 0 0)
          p (->Point 0 1)]
      (is (= (transform t p)
             (->Point 1 0)))
      (is (= (untransform t (->Point 1 0))
             (->Point 0 1))))

    (let [t (->Transformation 1 0 1 0 1 1)
          p (->Point 0 0)]

      (is (= (transform t p)
             (->Point 1 1)))

      (is (= (untransform t (->Point 1 1))
             (->Point 0 0))))))

(deftest test-mercator-projection
  (let [m (->MercatorProjection 10 (->Transformation 1 0 0 0 1 0))]
    (is (= (location-coordinate m (->Location 0 0))
           (->Coordinate 0 0 10)))))

