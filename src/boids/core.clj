(ns boids.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [boids.setup :as setup]
            [boids.globals :as globals]
            [boids.update :as update]
            [boids.draw :as draw]
            [boids.events :as events]))

(q/defsketch boids
  :title globals/title
  :size [(:width globals/screen-size) (:height globals/screen-size)]
  ; setup function called only once, during sketch initialization.
  :setup setup/setup
  ; update-state is called on each iteration before draw-state.
  :update update/update-state
  :draw draw/draw-state
  :mouse-clicked events/mouse-clicked
  :features [:keep-on-top :exit-on-close]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
