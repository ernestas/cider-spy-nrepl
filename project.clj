(defproject cider-spy/cider-spy-nrepl "0.1.0-SNAPSHOT"
  :description "Spy on CIDER to get useful REPL summary information."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [cider/cider-nrepl "0.1.0-SNAPSHOT"]
                 ]
  :profiles {:dev {:repl-options {:nrepl-middleware [cider-spy-nrepl.middleware.summary/wrap-info]}
                   }})
