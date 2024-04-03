(defn compose [f]
    (letfn [(eval [& args]
                (if (every? number? args)
                    (apply f args)
                    (apply mapv eval args)))]
        (fn [& args] (apply eval args))
    )
)

(def v+ (compose +))
(def v- (compose -))
(def v* (compose *))
(def vd (compose /))

(defn scalar [a, b] (reduce + (v* a b)))
(defn vect [a b]
    (letfn
        [(calc [i j] (- (* (nth a i) (nth b j)) (* (nth a j) (nth b i))))]
        [(calc 1 2) (calc 2 0) (calc 0 1)]))
(defn v*s [a sc] ((compose (fn [x] (* sc x))) a))

(def m+ v+)
(def m- v-)
(def m* v*)
(def md vd)

(def m*s v*s)
(defn transpose [m] (apply mapv vector m))
(defn m*v [m v] (mapv #(scalar % v) m))
(defn m*m [a b] (mapv (partial m*v (transpose b)) a))




