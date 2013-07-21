(ns nnp.mainwin
  (:use seesaw.core)
  (:use clojure.string)
  (:require [nnp.file-browser :as fb :only [default-width
                                            default-height
                                            save-file
                                            open-file
                                            save-open-button
                                            file-browser
                                            current-path
                                            file-name
                                            open?]])
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

(def edit-area
  "Text area to be wrapped in scrollable"
  (text :multi-line? true
                    :tab-size 4))

(def text-area
  "Main text area for the editor."
  (scrollable edit-area))

(def main-frame
  (frame :title (str "NNP .::. " fb/current-path (text fb/file-name))
                 :id main-frame
                 :menubar menu-bar
                 :content text-area
                 :size [fb/default-width :by fb/default-height]
                 :minimum-size [fb/default-width :by fb/default-height]
                 :on-close :exit))

(def main-window
  "Main window of notepad"
  (invoke-later
   (show! main-frame)))

(listen fb/save-open-button :action
                 ;TODO More efficient reading in of file to memory
                 (fn [e]
                   (let [file (str fb/current-path (text fb/file-name))]
                     (if fb/open?
                       (do (scroll! text-area :to :top)
                           (text! edit-area (slurp file)))
                       (spit file (text edit-area)))
                     (config! fb/file-browser :visible? false)
                     (println file)
                     (config! main-frame :title file))))
