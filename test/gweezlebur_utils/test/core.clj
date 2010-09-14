(ns gweezlebur-utils.test.core
  (:use [gweezlebur-utils.core] :reload-all)
  (:use [clojure.test]))


(let [test-xml-string "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>
<note>
  <to>Michael</to>
  <headers>
    <date>2010-09-14</date>
    <subject>Testing</subject>
  </headers>
  <body>Don't forget me to write your tests!</body>
</note>"
      xmldoc (xml-parse-string test-xml-string)]
  (deftest t-xml-parse-string   
    (is (map? xmldoc))
    (is (= (:tag xmldoc) :note))
    (is (= (:tag (first (:content xmldoc)) :to))))

  (deftest t-xml2map
    (let [note (xml2map xmldoc)]
      (is (map? note))
      (is (= (:to note) "Michael"))
      (is (= (-> note :headers :subject) "Testing"))
    )
  )
  )