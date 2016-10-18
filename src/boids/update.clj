(ns boids.update
   (:require [boids.utils :as util]
             [boids.globals :as globals]
             [boids.steer :as steer]))


(defn update-boid
   [boid boids]
   "
      Update boid position based on velocity
      Update velocity and heading angle based on other boids within influence distance
   "
   (let [new-velocity (steer/steer-boid boid boids)]
      {:x (move-x boid) ;; return
       :y (move-y boid)
       :vel-x (:vel-x new-velocity)
       :vel-y (:vel-y new-velocity)
       }))

(defn update-boids
   [state]
   "loop through and update all boids"
   (loop [old-boids (:boids state)
          new-boids []]
      (if (not-empty old-boids)
         (do (conj new-boids (update-boid (first old-boids) (:boids state)))
            (recur (rest old-boids) new-boids))
         new-boids)))



(defn update-boid-speed
   [state]
   ""
   (:boid-speed state))

(defn update-state
   [state]
   "returns updated state"
   {:boids (update-boids state)
    :boid-diam (:boid-diam state)
    :boid-speed (update-boid-speed state)
    :frame (inc (or (:frame state) 0))})