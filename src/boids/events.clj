(ns boids.events
   (:require [quil.core :as q]))

(defn drop-bird-seed [state mouse-x mouse-y]
   "
      drop a bird-seed target at mouse pos,
      bird seed will be removed one a bird reaches it
   "
   (assoc state :targets (conj (:targets state) {:x mouse-x :y mouse-y})))

(defn mouse-clicked [state event]
   "performs all on mouse-clicked events"
   (let [mouse-x (:x event)
         mouse-y (:y event)
         button (:button event)]
      (when (and (= button :left) state event)
         (drop-bird-seed state mouse-x mouse-y))))

