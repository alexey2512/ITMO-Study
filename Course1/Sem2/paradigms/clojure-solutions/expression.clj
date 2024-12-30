(load-file "parser.clj")

; Function expressions
(defn variable [name] #(get % name))
(defn constant [num] (fn [_] num))
(defn Operation [op]
  (fn [& operands] (fn [args] (apply op (mapv #(% args) operands)))))

(defn dvd
  ([frt] (/ 1 (double frt)))
  ([frt & args] (reduce #(/ (double %1) (double %2)) frt args)))
(defn expectation [& args] (/ (apply + args) (count args)))
(defn sqr [x] (* x x))

(def add        (Operation +))
(def subtract   (Operation -))
(def multiply   (Operation *))
(def divide     (Operation dvd))
(def negate     subtract)

; MeanVarn
(def mean       (Operation expectation))
(def varn       (Operation (fn [& args] (- (apply expectation (mapv sqr args)) (sqr (apply expectation args))))))

; Means
(def arithMean  mean)
(def geomMean   (Operation (fn [& args] (Math/pow (Math/abs (apply * args)) (/ 1 (count args))))))
(def harmMean   (Operation (fn [& args] (dvd (apply expectation (mapv dvd args))))))

; SinCos
(def sin        (Operation #(Math/sin %)))
(def cos        (Operation #(Math/cos %)))

; SinhCosh
(def sinh       (Operation #(Math/sinh %)))
(def cosh       (Operation #(Math/cosh %)))

(def operations {'+         add
                 '-         subtract
                 '*         multiply
                 '/         divide
                 'negate    negate
                 'mean      mean
                 'varn      varn
                 'arithMean arithMean
                 'geomMean  geomMean
                 'harmMean  harmMean
                 'sin       sin
                 'cos       cos
                 'sinh      sinh
                 'cosh      cosh
                 })




; Object expressions
(defn proto-get
  [obj key] (get obj key))

(defn proto-call [obj func & args]
  (apply (proto-get obj func) args))

(defn method [key]
  (fn [obj & args] (apply proto-call obj key args)))

(def _toString (method :toString))
(def _evaluate (method :evaluate))
(def _postfix (method :postfix))

(defn Variable [name] (letfn [(toStr [] name)] {
   :toString toStr
   :postfix toStr
   :evaluate (fn [vars] (get vars (clojure.string/lower-case (subs name 0 1))))
}))

(defn Constant [value] (letfn [(toStr [] (str value))] {
    :toString toStr
    :postfix toStr
    :evaluate (fn [_] value)
}))

(defn createOperation [sign op] (fn [& args]
    (letfn [(toStr [isPrefix] (fn [] (str
                 "(" (if (= isPrefix 1) (str sign " ") "")
                (clojure.string/join " " (mapv (if (= isPrefix 1) _toString _postfix) args))
                (if (= isPrefix 1) "" (str " " sign)) ")")))] {
    :toString (toStr 1)
    :postfix (toStr 0)
    :evaluate (fn [vars] (apply op (mapv #(_evaluate % vars) args)))
})))

(def Add      (createOperation "+"      +))
(def Subtract (createOperation "-"      -))
(def Multiply (createOperation "*"      *))
(def Divide   (createOperation "/"      dvd))
(def Negate   (createOperation "negate" -))
(def Pow      (createOperation "pow"    #(Math/pow %1 %2)))
(def Log      (createOperation "log"    (fn [a b] (/ (Math/log (Math/abs (double b))) (Math/log (Math/abs (double a)))))))
(def Max      (createOperation "max"    (fn [& args] (reduce max args))))
(def Min      (createOperation "min"    (fn [& args] (reduce min args))))

(def objectOperations {
                       '+       Add
                       '-       Subtract
                       '*       Multiply
                       '/       Divide
                       'negate  Negate
                       'log     Log
                       'pow     Pow
                       'min     Min
                       'max     Max
                       })

(defn evaluate [expr vars] (_evaluate expr vars))
(defn toString [expr] (_toString expr))

(defn parseAll [ops cnstF varF] (fn [expression] (letfn
                                                 [(parse [element]
                                                    (cond
                                                      (list? element) (apply (get ops (first element)) (map parse (rest element)))
                                                      (number? element) (cnstF element)
                                                      :else (varF (str element))))]
                                                 (parse (read-string expression)))))

(def parseFunction (parseAll operations constant variable))
(def parseObject (parseAll objectOperations Constant Variable))





; Combinatoric Parser
(def *ws (+ignore (+star (+char " \t\n\r"))))
(def *number (+str (+seqf (fn [sign abs]
                          (if (= sign \-) (cons sign abs) abs))
                          (+opt (+char "-"))
                          (+plus (+char ".0123456789")))))
(def *constant (+map #(Constant (read-string %)) *number))
(def *word (+str (+plus (+char "xXyYzZ"))))
(def *variable (+map #(Variable %) *word))
(def opsSymbols (apply str (map name (keys objectOperations))))
(def *operation
  (+seqf
    (fn [args op] (apply (objectOperations (symbol op)) args))
    (+ignore (+char "(")) *ws
    (+plus (+seqn 0 (+or *constant *variable (delay *operation)) *ws))
    (+str (+plus (+char opsSymbols))) *ws
    (+ignore (+char ")"))))
(def parseObjectPostfix (+parser (+seqn 0 *ws (+or *constant *variable *operation) *ws)))
(def toStringPostfix _postfix)



