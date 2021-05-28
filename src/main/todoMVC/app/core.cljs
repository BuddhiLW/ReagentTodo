(ns todoMVC.app.core
  (:require [reagent.dom :as rdom]))

;; --- Views ---

(defn todo-input
  "todo-input component inside task-entry component"
  []
  [:input {:class "new-todo"
           :placeholder "Todo input"
           :type "text"}])

(defn task-list []
  "Tasks TODO section component"
  [:section.main
   [:div "Todo list"]])

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
    [:div
     [task-list]
     [footer-controls]]]
   [:footer.info
    [:p "footer indo"]]])

(defn render []
  (rdom/render [todo] (.getElementById js/document "root")))

(defn ^:export main []
  (render))

(defn ^:dev/after-load reload! []
  (render))
