(ns boids.update)

(defn update-boids
   [state]
   ""
   (:boids state))

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