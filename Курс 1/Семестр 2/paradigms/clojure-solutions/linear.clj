(defn compose [f]
    (fn [& args] (apply mapv f args))
)

(def v+ (compose +))
(def v- (compose -))
(def v* (compose *))
(def vd (compose /))

(defn scalar [a, b] (apply + (v* a b)))
(defn vect [a b]
    (letfn
        [(calc [i j] (- (* (nth a i) (nth b j)) (* (nth a j) (nth b i))))]
        [(calc 1 2) (calc 2 0) (calc 0 1)]))
(defn v*s [a sc] ((compose #(* sc %)) a))

(def m+ (compose v+))
(def m- (compose v-))
(def m* (compose v*))
(def md (compose vd))

(defn m*s [m sc] (mapv #(v*s % sc) m))
(defn transpose [m] (apply mapv vector m))
(defn m*v [m v] (mapv #(scalar % v) m))
(defn m*m [a b] (mapv (partial m*v (transpose b)) a))

(def c4+ (compose (compose m+)))
(def c4- (compose (compose m-)))
(def c4* (compose (compose m*)))
(def c4d (compose (compose md)))




