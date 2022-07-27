(ns creditor.creditor
  (:require [creditor.db.core :as db]
            [creditor.validation :refer [validator]]
            [creditor.validation :refer [validate-delete-item]]
            [creditor.validation :refer [validate-update-item]]))

(defn items
  ;; All items in the database
  []
  (db/items))

(defn total-credits
  ;;Amount of total credits earned
  []
  (apply + (map (fn [db-map] (:credit db-map)) (db/items))))

(defn total-debits
  ;;Amount of total debits acquired
  []
  (apply + (map (fn [db-map] (:debit db-map)) (db/items))))

(defn create-new-item [params]
  (if-let [errors (validator params)]
    (throw
     (ex-info "An exception occurred here."
              {:creditor/error-id :validation
               :errors errors}))
    (db/new-item! params)))

(defn delete-item-by-id [id]
  (if-let [item-error (validate-delete-item id)]
    (throw
     (ex-info "Id values produced an error"
              {:creditor/item-id-error :validation
               :error item-error}))
    (db/delete-item id)))

(defn update-item [params]
  (if-let [update-errors (validate-update-item params)]
    (throw
     (ex-info "Error occurred when trying to update an item"
              {:creditor/update-item-error :validation
               :errors update-errors}))
    (db/update-item params)))

