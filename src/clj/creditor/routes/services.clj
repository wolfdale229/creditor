(ns creditor.routes.services
  (:require [ring.util.http-response :as response]
            [creditor.creditor :as creditor]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.coercion :as coercion]
            [reitit.coercion.spec :as spec-coercion]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.parameters :as parameters]
            [creditor.middleware.formats :as formats]
            ))



(defn services []
  ["/api" {:middleware [parameters/parameters-middleware
                        muuntaja/format-negotiate-middleware
                        muuntaja/format-request-middleware
                        muuntaja/format-response-middleware
                        coercion/coerce-request-middleware
                        coercion/coerce-response-middleware
                        exception/exception-middleware
                        multipart/multipart-middleware]
           :muuntaja formats/instance
           :coercion spec-coercion/coercion
           :swagger {:id ::api}}
   ["" {:no-doc true}
    ["/swagger.json" {:get
                      (swagger/create-swagger-handler)}]
    ["/swagger-ui*" {:get (swagger-ui/create-swagger-ui-handler
                           {:url "/api/swagger.json"})}]]
   ["/creditor"
    ["/"
     {:get
      {:parameters nil
       :responses {200
                   {:body
                    [{:id int?
                      :description string?
                      :credit int?
                      :debit int?}]}}
       :handler (fn [_]
                  (response/ok (creditor/items)))}}]

    ["/credits"
     {:get
      {:parameters nil
       :responses {:200
                   {:body map?}}
       :handler (fn [_]
                  (response/ok {:status (str (creditor/total-credits))}))}}]

    ["/debits"
     {:get
      {:parameters nil
       :responses {200
                   {:body map?}}
       :handler (fn [_]
                  (response/ok {:status (creditor/total-debits)}))}}]

    ["/new-item"
     {:post
      {:parameters {:body
                    {:description string?
                     :credit int?
                     :debit int?}}
       :responses {200 {:body map?}
                   400 {:body map?}
                   500 {:errors map?}}
       :handler (fn [{{params :body} :parameters}]
                  (try
                    (creditor/create-new-item params)
                    (response/ok {:status :ok})
                    (catch Exception e
                      (let [{id :creditor/error-id
                             errors :errors} (ex-data e)]
                        (case id
                          :validation
                          (response/bad-request {:errors errors})
                          ;;else
                          (response/internal-server-error
                           {:errors
                            {:server-error ["Failed to perform save item operation."]}}))))))}}]

    ["/update-item"
     {:put
      {:parameters {:body
                    {:id int?
                     :description string?
                     :credit int?
                     :debit int?}}
       :responses {200 {:body map?}
                   400 {:body map?}
                   500 {:errors map?}}
       :handler (fn [{{params :body} :parameters}]
                  (try
                    (creditor/update-item params)
                    (response/ok {:status :updated})
                    (catch Exception e
                      (let [{id :creditor/update-item-error
                             errors :errors} (ex-data e)]
                        (case id
                          :validation
                          (response/bad-request {:errors errors})
                          ;; else
                          (response/internal-server-error
                           {:errors
                            {:server-error ["Failed to perform update item operation."]}}))))))}}]

    ["/delete-item"
     {:delete
      {:parameters {:body
                    {:id int?}}
       :responses {200 {:body map?}
                   400 {:body map?}
                   500 {:errors map?}}
       :handler (fn [{{params :body} :parameters}]
                  (try
                    (creditor/delete-item-by-id params)
                    (response/ok {:status :deleted})
                    (catch Exception e
                      (let [{id :creditor/item-id-error
                             errors :error} (ex-data e)]
                        (case id
                          :validation
                          (response/bad-request {:errors errors})
                          ;; else
                          (response/internal-server-error
                           {:error
                            {:server-error ["Failed to perform delete item operation."]}}))))))}}]

    ]])
