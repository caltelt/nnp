(ns nnp.gui.mainwin
  (:use seesaw.core)
  (:use clojure.string)
  (:import java.io.File))

(native!)

(def default-width 600)
(def default-height 480)

(defn seq-of-files [dir]
  "Returns a seq of files for the directory given."
  (.listFiles (File. dir)))

(defn list-of-files [fileseq]
  "Returns seq of all directories in fileseq"
  (filter #(true? (.isDirectory %)) fileseq))

(defn list-of-dirs [fileseq]
  "Returns seq of all files in fileseq"
  (filter #(and (false? (.isDirectory %)) (false? (.isHidden %))) fileseq))

;; (join "\n" (map #(.getName %) (concat (list-of-files (seq-of-files "./")) (list -of-dirs (seq-of-files "./")))))

(def file-list-box
  "list of folders and files to select"
  (scrollable
   (listbox :model (concat (list-of-files (seq-of-files "./")) (list-of-dirs (seq-of-files "./"))))))

(def file-path
  "Current path"
  (text :text (.getCanonicalPath (File. "./"))
                     :columns 1
                     :editable? true))

(def file-name
  "Name for current file to be saved/selected in fb"
  (text :editable? true))

(def file-browser-panel
  "Panel to be added to the file-browser window"
  (border-panel
   :north file-path
   :center file-list-box
   :south file-name
   :vgap 5 :hgap 5 :border 5))

(def file-browser
  "File browser for viewing directories and opening/saving files."
  (frame :title "File Browser"
         :id file-browser
         :content file-browser-panel
         :size [default-width :by default-height]
         :minimum-size [default-width :by default-height]
         :on-close :hide))

;;--------------------------------------------------
;;--------------------------------------------------
;;--------------------------------------------------
;;Create the items for the file-menu

(def file-menu-save
  (menu-item :text "Save"
             :listen [:action (fn [e]
                                (show! file-browser))]))

(def file-menu-open
  (menu-item :text "Open"
             :listen [:action (fn [e]
                                (show! file-browser))]))

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
                 :size [default-width :by default-height]
                 :minimum-size [default-width :by default-height]
                 :on-close :exit))))
