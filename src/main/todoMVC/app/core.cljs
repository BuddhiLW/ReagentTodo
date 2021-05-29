(ns todoMVC.app.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [clojure.string :as str]
            [cljs.pprint :as pp]))

;; --- App State ---

;; (def initial-todos
;;   {1 {:id 1, :title "Lavar roupa (do laudry)" :done false} 
;;    3 {:id 3, :title "Estudar Mandarim (study mandarim)" :done false} 
;;    2 {:id 2, :title "Cuidar dos pets (take care of pets)" :done false}})

;; (def initial-todos-sorted
;;   (into (sorted-map) initial-todos))

(defonce todos (r/atom (sorted-map)))

(defonce counter (r/atom 0))

;; Explaination of why to use atoms
;; An atom is a mutable wrapper around a immutable data-structure.

;; The application-state can be updated using atoms. Switching out the mutable representation of our state within it.


;; --- Watch the State ---

(add-watch todos :todos
           (fn [key _atom _old-state new-state]
             (println "---" key "atom changed ---")
             (pp/pprint new-state)))

;; --- Utilities ---

(defn add-todo [text]
  (let [id (swap! counter inc)
        new-todo {:id id, :title text, :done false}]
    (swap! todos assoc id new-todo)))

(defn delete-todo [id]
  (swap! todos dissoc id))

;; --- Initialize App with Sample Data ---

(defonce init (do
                (add-todo "Lavar roupa (do laudry)")
                (add-todo "Estudar Mandarim (study mandarim)")
                (add-todo "Cuidar dos pets (take care of pets)")))

;; --- Views ---

(defn todo-input
  "todo-input component inside task-entry component"
  []
  (let [input-text (r/atom "")
        update-text #(reset! input-text %)
        stop #(reset! input-text "")
        save #(let [trimmed-text (-> @input-text str str/trim)]
                (if-not (empty? trimmed-text) (add-todo trimmed-text))
                (stop))
        key-pressed #(case %
                       "Enter" (save)
                       "Esc" (stop)
                       "Ecape" (stop)
                       nil)]
    
    (fn [] ;; => form-two reagent component
      [:input {:class "new-todo"
               :placeholder "Todo input"
               :type "text"
               :value @input-text
               :on-blur save
               :on-change #(update-text (.. % -target -value))
               :on-key-down #(key-pressed (.. % -key))}])))

(defn todo-item [{:keys [id title]}]
  [:li
   [:div.view
    [:label title]
    [:button.destroy {:on-click #(delete-todo id)}]]])

(defn task-list []
  "Tasks TODO section component"
  (let [items (vals @todos)]
    [:section.main
     [:ul.todo-list
      (for [todo items]
        ^{:key (:id todo)} [todo-item todo])]]))

(defn footer-controls
  "footer with control utilities"
  []
  [:footer.footer
   [:div "Footer control"]])

(defn task-entry
  "task-entry component"
  []
  [:header.header
   [:h1 "todos"]
   [todo-input]])

(defn todo []
  [:div
   [:section.todoapp
    [task-entry]
    (when (seq @todos)
      [:div
       [task-list]
       [footer-controls]])]
   [:footer.info
    [:p "footer info"]]])

(defn render []
  (rdom/render [todo] (.getElementById js/document "root")))

(defn ^:export main []
  (render))

(defn ^:dev/after-load reload! []
  (render))
