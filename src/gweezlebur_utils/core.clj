(ns gweezlebur-utils.core
  (:require [clojure.xml :as xml]))

(defn xml-parse-string
  "Convert a string containing an XML document into a clojure/xml structure"
  [s]
  (xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))

(defn xmlel2mapel
  "Recursive piece of xml2map"
  [el]
  (let [content (:content el)]
    (hash-map
     (:tag el)
     (cond (nil? content) nil
           (and (= (count content) 1)
                (instance? String (first content))) (first content)
                :else (apply merge (map xmlel2mapel content))))))

(defn xml2map
  "Convert a clojure/xml structure into a hashmap"
  [root]
  (apply merge (map xmlel2mapel (:content root))))
