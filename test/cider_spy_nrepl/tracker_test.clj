(ns cider-spy-nrepl.tracker-test
  (:require [cider-spy-nrepl.tracker :refer :all]
            [clojure.test :refer :all]))

(defmacro tracker-harness [& forms]
  `(do
     (reset! messages '())
     (reset! files-loaded {})
     (reset! trail-atom '())
     (reset! commands-atom {})
     ~@forms))

;; Even though I'll remove tracking of whole messages, I'll test for now.
(deftest test-messages
  (tracker-harness
   (let [msg1 {:ns "foo-ns"}
         msg2 {:ns "foo2-ns"}]
     (track-msg! msg1)
     (track-msg! msg2)
     (is (= (list msg2 msg1) @messages)))))

(deftest test-track-namespace
  (tracker-harness
   (track-msg! {:ns "foo-ns"})
   (is (= (list "foo-ns") (map :ns @trail-atom)))))

(deftest test-track-command
  (tracker-harness
   (let [code "(println \"bob\")"]
     (track-msg! {:code code})
     (is (= 1 (get @commands-atom code)))
     (track-msg! {:code code})
     (is (= 2 (get @commands-atom code))))))

(deftest test-track-file-loaded
  (tracker-harness
   (let [file-path "some-file"]
     (track-msg! {:op "load-file" :file-path file-path})
     (is (= 1 (get @files-loaded file-path)))
     (track-msg! {:op "load-file" :file-path file-path})
     (is (= 2 (get @files-loaded file-path))))))
