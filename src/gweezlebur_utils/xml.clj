(ns gweezlebur-utils.xml
  (:require [clojure.xml :as xml]))


(defn xml-parse-string [s]
  (xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))
