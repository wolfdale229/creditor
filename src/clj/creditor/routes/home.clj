(ns creditor.routes.home
  (:require
   [creditor.layout :as layout]
   [creditor.db.core :as db]
   [creditor.middleware :as middleware]
   [ring.util.http-response :as response]))


(defn home-page [request]
  (layout/render request "home.html" {:items (db/items)}))

(defn create-item [{:keys [params]}]
  (db/new-item! params)
  (response/found "/"))

(defn about-page [request]
  (layout/render request "about.html"))

(defn home-routes []
  [ "" 
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/create" {:post create-item}]
   ["/about" {:get about-page}]])

