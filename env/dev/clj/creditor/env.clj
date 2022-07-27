(ns creditor.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [creditor.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[creditor started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[creditor has shut down successfully]=-"))
   :middleware wrap-dev})
