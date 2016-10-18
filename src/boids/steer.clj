(ns boids.steer)

(defn add-boid
   [acc next-boid]
   "
      add boid positions and velocities to acc
      [acc] the current accumlated map
      [next-boid] the next boid to add
   "
   (doseq [x (keys acc)]
      (assoc acc x (+ (x acc) (x next-boid)))))

(defn get-average-fn
   [n]
   #((doseq [x (keys %)]
        (assoc % x (/ (x %) n)))))

(defn alignment
   [avg-velocity]
   "
      Steer towards the average heading of local flockmates
      use average velocities in [avg-boids]
      return average heading angle
   "

   )

(defn cohesion
   [boid avg-position]
   "
      steer to move toward the average position of local flockmates
      use [avg-boids] position to get and return angle that would move
      boid towards the average position
   ")

(defn seperation
   [boid boids]
   "
      steer to avoid crowding local flockmates
      if there are one or more boids that are too close,
      return a heading opposite of the average of those boids
      else, return nil
   ")

(defn get-new-velocity
   [boid boids]
   "
      using average velocities of boids within influence distance
      to calculate and return new velocity

      TODO: boid shouldn't include itself in averages
   "
   (let [boids-in-range (filter #(util/in-range? boid %) boids)] ;; filter out boids that aren't in range
      (map (get-average-fn (alength boids-in-range))
           (reduce add-boids {:x 0 :y 0 :vel-x 0 :vel-y 0} boids-in-range)))) ;; add boids together

(defn steer-boid
   [boid boids])