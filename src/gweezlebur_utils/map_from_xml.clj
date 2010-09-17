(ns gweezlebur-utils.map-from-xml)

(defn from-xmlel
  "Recursive piece of from-xml"
  [el]
  (let [content (:content el)
        text (first content)
        type (:type (:attrs el))
        datefmt (java.text.SimpleDateFormat. "yyyy-MM-dd")
        datetimefmt (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ssZ")]
     (hash-map
      (:tag el)
      (cond (or (> (count content) 1)
                (= (:type (:attrs el)) "array")) (apply vector (map from-xmlel content))
            (nil? content) nil
            (and (= (count content) 1)
                 (instance? String text))
                 (condp = type
                     "integer" (Integer. text)
                     "boolean" (or (= text "true")
                                   (= text "1"))
                     "date" (.parse datefmt text)
                     "datetime" (.parse datetimefmt text)
                     "decimal" (java.math.BigDecimal. text)
                     "float"   (. Float parseFloat text)
                     text)
            :else (apply merge (map from-xmlel content))))))

(defn from-xml
  "Convert a clojure/xml structure into a map"
  [root]
  (apply merge (map from-xmlel (:content root))))
