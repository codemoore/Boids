(ns boids.draw
   (:require [quil.core :as q]
             [boids.globals :as globals]
             [boids.utils :as util]))

(defn draw-boid
   [boid diameter]
   "draw a single boid"
   (apply q/fill globals/boid-fill-color)
   (apply q/stroke globals/vel-stroke-color)
   (q/stroke-weight globals/boid-stroke-weight)
   (let [x (/ (:width globals/screen-size) 2)
         y (/ (:height globals/screen-size) 2)
         bx (:x (:pos boid))
         by (:y (:pos boid))
         r (/ diameter 2)
         ;angle (util/calc-angle (:vel boid))
         norm-vel (util/normalize-velocity (:vel boid))]
      (q/push-matrix) ;; save previous state
      (q/translate bx by) ;; move origin to boid position
      (q/line 0 0 (* (:x norm-vel) diameter) (* (:y norm-vel) diameter))
      (apply q/stroke globals/boid-stroke-color)
      ;(println (str (:vel boid) " angle: " angle))
      ;(q/rotate   (+ angle (/ Math/PI 2))) ;; rotate to direction of movement
      (q/ellipse 0 0 diameter diameter)
      (q/pop-matrix))) ;; return to previous state


(defn draw-boids
   [state]
   "loop through boids and draw them"
   (let [diam (:boid-diam state)]
      (loop [boids (:boids state)]
         (when (not-empty boids)
            (draw-boid (first boids) diam)
            (recur (rest boids))))))


(defn draw-bird-seed [bird-seed diam]
   (q/push-matrix)
   (apply q/fill globals/bird-seed-color)
   (q/translate (:x bird-seed) (:y bird-seed))
   (q/ellipse 0, 0, diam diam)
   (q/pop-matrix))

(defn draw-targets
   [state]
   "draw bird-seed targets"
   (let [diam globals/bird-seed-diam]
      (loop [targets (:targets state)]
         (when (not-empty targets)
            (draw-bird-seed (first targets) diam)
            (recur (rest targets))))))

(defn draw-state
   [state]
   "draws to screen based on [state]"
   (q/clear)
   (apply q/background globals/background-color)
   (draw-targets state)
   (draw-boids state))



