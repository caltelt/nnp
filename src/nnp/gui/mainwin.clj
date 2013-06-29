(ns nnp.gui.mainwin
  (:use seesaw.core)
  (:use clojure.string)
  ;(:require [clojure.java.io :as io]))
  (:import java.io.File))

(native!)

(def default-width 600)
(def default-height 480)

(defn seq-of-files [dir]
  "Returns a seq of files for the directory given."
;  (file-seq (io/file dir)))
  (.listFiles (File. dir)))

(defn list-of-files [fileseq]
  "Returns seq of all directories in fileseq"
  (filter #(true? (.isDirectory %)) fileseq))

(defn list-of-dirs [fileseq]
  "Returns seq of all files in fileseq"
  (filter #(and (false? (.isDirectory %)) (false? (.isHidden %))) fileseq))

(def lbl (scrollable
          (text :text (join "\n" (map #(.getName %) (concat
                                                     (list-of-files (seq-of-files "./"))
                                                     (list-of-dirs (seq-of-files "./")))))
                :multi-line? true
                :editable? false)))


(def file-browser
  "File browser for viewing directories and opening/saving files."
   (-> (frame :title "File Browser"
              :id file-browser
              :content lbl
              :size [default-width :by default-height]
              :minimum-size [default-width :by default-height]
              :on-close :hide)))

;;Create the items for the file-menu

(def file-menu-save
  (menu-item :text "Save"
             :listen [:action (fn [e]
                                (show! file-browser))]))

(def file-menu-open
  (menu-item :text "Open"
             :listen [:action (fn [e]
                                (alert "OPENED"))]))

;;End creating items for file menu

;;Create menu items

(def file-menu
  (menu :text "File"
        :items [file-menu-save
                file-menu-open]))

;;End creating menu items


;;Create main window

(def menu-bar
  (menubar :items [file-menu]))

(def text-area
  "Main text area for the editor."
  (scrollable (text :multi-line? true)))

(def main-window
   (invoke-later
    (-> (frame :title "Hello"
               :id main-window
               :menubar menu-bar
               :content text-area
               :size [default-width :by default-height]
               :minimum-size [default-width :by default-height]
               :on-close :exit)
        show!)))
