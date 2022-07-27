(ns creditor.validation
  (:require [struct.core :as st]))

(def delete-item-schema
  [[:id
    st/required
    st/integer
    {:id "Id must be an integer value"
     :validate (fn [id] (int? id))}]])

(def create-item-schema
  [[:description
    st/required
    st/string
    {:description "Write a short description, up to hundred words"
     :validate (fn [desc] (<= (count desc) 100))}]
   [:credit
    st/required
    st/integer]
   [:debit
    st/required
    st/integer]])

(def update-item-schema
  [[:description
    st/required
    st/string
    {:description "Write a short description, up to hundred words"
     :validate (fn [desc] (<= (count desc) 100))}]
   [:credit
    st/required
    st/integer]
   [:debit
    st/required
    st/integer]
   [:id
    st/required
    st/integer]])

(defn validate-delete-item [id]
  (first (st/validate id delete-item-schema)))

(defn validator [data]
  (first (st/validate data create-item-schema)))

(defn validate-update-item [params]
  (first (st/validate params update-item-schema)))
