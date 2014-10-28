(ns modestmaps-cljs.core-test
  (:require
   [clojure.test :refer :all]
   [modestmaps-cljs.core :refer :all]))


(deftest test-coordinate
  (let [coordinate (->Coordinate 0.00 1.00 2.00)]
    (testing "#Coordinate{:row 0 :column 1 :zoom 2}"
      (testing "(zoom-to coordinate)"
        (is (= (zoom-to coordinate 3.00)
               (->Coordinate 0.00 2.00 3.00)))

        (is (= (zoom-to (->Coordinate 0.00 2.00 3.00) 1.00)
               (->Coordinate 0.00 0.500 1.00))))

      (testing "(zoom-by coordinate"
        (is (= (zoom-by coordinate 3.00) (->Coordinate 0.00 8.00 5.00))))

      (testing "(up coordinate)"
        (is (= (up coordinate 1.0) (->Coordinate -1.00 1.00 2.00))))

      (testing "(right coordinate"
        (is (= (right coordinate 1.0) (->Coordinate 0.00 2.00 2.00))))

      (testing "(down coordinate"
        (is (= (down coordinate 1.0) (->Coordinate 1.00 1.00 2.00))))

      (testing "(left coordinate)"
        (is (= (left coordinate 1.0) (->Coordinate 0.00 0.00 2.00)))))))


