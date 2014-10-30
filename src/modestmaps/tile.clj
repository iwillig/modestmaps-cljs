(ns modestmaps.tile)

(defn n-tiles-in-zoom [zoom]
  {:pre  [(pos? zoom)]}
  (reduce (fn [n i] (+ n (Math/pow 4 i))) (range (+ 1 zoom))))

;;(assert (= (n-tiles-in-zoom 10) 1398100.0))
;;(assert (= (n-tiles-in-zoom 1) 4.0)) ;; python n_tiles_in_zoom(1) -> 5
;;(assert (= (n-tiles-in-zoom 3) 84.0))

