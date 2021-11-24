(defproject org.clojars.bfollek/rabbithole "0.2.2-SNAPSHOT"
  :description "Utility functions"
  :url "https://github.com/bfollek/rabbithole"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :deploy-repositories [["clojars" {:url "https://clojars.org/repo"
                                    :username :env/clojars_user
                                    :password :env/clojars_deploy_token
                                    :sign-releases true}]])