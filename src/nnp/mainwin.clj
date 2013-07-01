(ns nnp.mainwin
  (:use seesaw.core)
  (:use clojure.string)
  (:require [nnp.file-browser :as fb :only [default-width default-height
                                            save-file open-file
                                            file-browser]])
  (:import java.io.File))

(native!)

;;--------------------------------------------------
;;--------------------------------------------------
;;--------------------------------------------------
;;Create the items for the file-menu

(def file-menu-save
  (menu-item :text "Save"
             :listen [:action (fn [e]
                                (fb/save-file "file"))]))

(def file-menu-open
  (menu-item :text "Open"
             :listen [:action (fn [e]
                                (fb/open-file "file"))]))

;;--------------------------------------------------
;;--------------------------------------------------
;;--------------------------------------------------
;;Create menu items

(def file-menu
  (menu :text "File"
        :items [file-menu-save
                file-menu-open]))

;;End creating menu items


;;--------------------------------------------------
;;--------------------------------------------------
;;--------------------------------------------------
;;Create main window

(def menu-bar
  (menubar :items [file-menu]))

(def text-area
  "Main text area for the editor."
  (scrollable (text :multi-line? true
                    :tab-size 4)))

(def main-window
  "Main window of notepad"
  (invoke-later
   (show! (frame :title "Hello"
                 :id main-window
                 :menubar menu-bar
                 :content text-area
                 :size [fb/default-width :by fb/default-height]
                 :minimum-size [fb/default-width :by fb/default-height]
                 :on-close :exit))))
