(ns nnp.gui.mainwin
  (:use seesaw.core))

(def def-height 600)
(def def-width 480)

(def text-area (text :multi-line? true))

(def main-window
   (invoke-later
    (-> (frame :title "Hello"
               :content text-area
               :on-close :exit)
        pack!
        show!)))

