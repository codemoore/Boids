(ns boids.draw
   (:require [quil.core :as q]
             [boids.globals :as globals]))

(defn draw-boid
   [boid diameter]
   "draw a single boid"
   (apply q/fill globals/boid-fill-color)
   (apply q/stroke globals/boid-stroke-color)
   (q/stroke-weight globals/boid-stroke-weight)
   (q/ellipse (:x boid) (:y boid) diameter diameter))

(defn draw-boids
   [state]
   "loop through boids and draw them"
   (let [diam (:boid-diam state)]
      (loop [boids (:boids state)]
         (when (not-empty boids)
            (draw-boid (first boids) diam)
            (recur (rest boids))))))

(defn draw-state
   [state]
   "draws to screen based on [state]"
   (q/clear)
   (apply q/background globals/background-color)
   (draw-boids state))



