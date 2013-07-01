(ns nnp.file-browser
  (:use seesaw.core)
  (:use seesaw.keymap)
  (:use clojure.string)
  (:import java.io.File)
  (:import java.awt.event.KeyEvent))


(def default-width 600)
(def default-height 480)

(def current-path "./")

;;--------------------------------------------------

(defn seq-of-files [dir]
  "Returns a seq of files for the directory given."
  (.listFiles (File. dir)))

(defn list-of-files [fileseq]
  "Returns seq of all directories in fileseq"
  (filter #(true? (.isDirectory %)) fileseq))

(defn list-of-dirs [fileseq]
  "Returns seq of all files in fileseq"
  (filter #(and (false? (.isDirectory %)) (false? (.isHidden %))) fileseq))

(defn list-of-files-dirs
  [filepath]
  (concat (list-of-files (seq-of-files filepath)) (list-of-dirs (seq-of-files filepath))))

;;--------------------------------------------------

(def file-list-box
  "list of folders and files to select"
   (listbox :model (list-of-files-dirs current-path)))

(def file-path
  "Current path"
  (text :text (.getCanonicalPath (File. current-path))
        :columns 1
        :editable? true))

(def file-name
  "Name for current file to be saved/selected in fb"
  (text :editable? true))

(def file-browser-panel
  "Panel to be added to the file-browser window"
  (border-panel
   :north file-path
   :center (scrollable file-list-box)
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

(defn save-file
  "Function called when save is done"
  [file]
  (config! file-name :text file)
  (show! file-browser))

(defn open-file
  "Function called when open is done"
  [file]
  (config! file-name :text file)
  (show! file-browser))

(listen file-list-box :selection
        (fn [e]
          (when-let [s (selection e)]
            (text! file-name (.getName s)))))

;;TODO add '/' at end of filepath
(map-key file-path "ENTER"
         (fn [e]
           (def current-path (text file-path))
           (config! file-list-box :model (list-of-files-dirs current-path))))

