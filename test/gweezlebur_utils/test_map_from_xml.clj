(ns gweezlebur-utils.test-map-from-xml
  (:use [gweezlebur-utils.map-from-xml] :reload-all)
  (:use clojure.test)
  (:require clojure.xml))

;; copied from gweezlebur-utils.xml
(defn xml-parse-string [s]
  (clojure.xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))

(testing "map/from-xml"
  (let [album-xml-str
        "<album>
          <title>Ukulele Whore</title>
          <artist-name>Jennifer Teeter</artist-name>
          <id type=\"integer\">1471</id>
          <approved type=\"boolean\">true</approved>
          <featured type=\"boolean\">false</featured>
          <mp3-only type=\"boolean\">1</mp3-only>
          <favorites-count type=\"integer\">10</favorites-count>
          <recorded-on type=\"date\">2010-07-16</recorded-on>
          <listed-at type=\"datetime\">2010-07-16T09:28:00+0000</listed-at>
          <artist-email-address>jteeter@example.com</artist-email-address>
          <publisher-id></publisher-id>
          <average-rating type=\"decimal\">4.75</average-rating>
          <suggested-volume type=\"float\">11</suggested-volume>
          <images>
            <image>jt_coverart.jpg</image>
            <image>jt_backcover.jpg</image>
          </images>
          <comments type=\"array\" />
          <links type=\"array\">
            <website>http://jenniferteeter.com</website>
          </links>
        </album>"
        album-xml (xml-parse-string album-xml-str)
        album (from-xml album-xml)

        datefmt (java.text.SimpleDateFormat. "yyyy-MM-dd")
        datetimefmt (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ssZ")]

    (deftest basic-compostion
      (is (map? album)))

    (deftest standard-elements
      (is (= (album :title) "Ukulele Whore"))
      (is (= (album :artist-name) "Jennifer Teeter"))
      (is (= (album :artist-email-address) "jteeter@example.com"))

    (deftest converted-elements
      (is (= (album :id) 1471))
      (is (true? (album :approved)) "Boolean: true")
      (is (false? (album :featured)) "Boolean: false")
      (is (true? (album :mp3-only)) "Boolean: 1")
      (is (= (album :favorites-count) 10))
      (is (= (album :recorded-on) (.parse datefmt "2010-07-16")))
      (is (= (album :listed-at) (.parse datetimefmt "2010-07-16T09:28:00+0000")))
      (is (= (album :average-rating) (java.math.BigDecimal. "4.75")))
      (is (= (album :suggested-volume) 11.0))))

    (deftest nil-elements
      (is (nil? (album :publisher-id)) "Empty element in the document")
      (is (nil? (album :compilation)) "Element not in the document"))

    (deftest element-arrays
      (is (vector? (album :images)))
      (some #{{:image "jt_coverart.jpg"}} (album :images))
      (some #{{:image "jt_backcover.jpg"}} (album :images))
      (is (vector? (album :comments)) "Empty but type=array")
      (is (vector? (album :links)) "Single element but type=array")
      (is (= (first (album :links)) {:website "http://jenniferteeter.com"})))))