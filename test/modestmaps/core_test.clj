(ns modestmaps.core-test
  (:require
   [clojure.test :refer :all]
   [modestmaps.core :refer :all]))

(deftest test-coordinate
  (let [coordinate (->Coordinate 0.00 1.00 2.00)]
    (testing "#Coordinate{:row 0 :column 1 :zoom 2}"
      (testing "(zoom-to coordinate)"
        (is (= (zoom-to coordinate 3.00)
               (->Coordinate 0.00 2.00 3.00)))

        (is (= (zoom-to (->Coordinate 0.00 2.00 3.00) 1.00)
               (->Coordinate 0.00 0.500 1.00))))

      (testing "(zoom-by coordinate)"
        (is (= (zoom-by coordinate 3.00) (->Coordinate 0.00 8.00 5.00))))

      (testing "(up coordinate)"
        (is (= (up coordinate 1.0) (->Coordinate -1.00 1.00 2.00))))

      (testing "(right coordinate)"
        (is (= (right coordinate 1.0) (->Coordinate 0.00 2.00 2.00))))

      (testing "(down coordinate"
        (is (= (down coordinate 1.0) (->Coordinate 1.00 1.00 2.00))))

      (testing "(left coordinate)"
        (is (= (left coordinate 1.0) (->Coordinate 0.00 0.00 2.00)))))))


(deftest test-extent
  (let [ext (make-extent -10 -10 10 10)]
    (testing "properly initializes its sides"
      (are [x y] (= x y)
           (:west ext)  -10
           (:south ext) -10
           (:north ext)  10
           (:east ext)   10))

    (testing "expands to fit a location"
      (let [ext (enclose-location ext (->Location -40 -40))]
        (is (= (:west ext) -40))
        (is (= (:south ext) -40))))

    (testing "expands to fit many locations"
      (let [ext (enclose-locations ext [(->Location -40 -40)
                                        (->Location  40  40)])]
        (is (= (:west ext) -40))
        (is (= (:east ext) 40))
        (is (= (:south ext) -40))
        (is (= (:north ext) 40))))

    (testing "knows when it contains a location"
      (is (contains-location? ext (->Location 0 0)))
      (is (not (contains-location? ext (->Location 0 90)))))))

