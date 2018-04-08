;;
;;	Sustained Attention to Response Task (SART)
;;
;;	In each trial the participant sees a letter: "O" or "Q".
;;	They must press a key every time an O appears (90% of trials),
;;	but withhold their response when the stimulus is a Q (10% of trials).
;;
;;	Practical Cognitive Science 2018
;;


;;===================;;
;;  Experiment code  ;;
;;===================;;


;; Experiment settings
(defvar *stimulus-duration* 2) ; number of seconds the stimulus is shown
(defvar *inter-stimulus-interval* 0.5) ; number of seconds between trials
; every number has a 1/10th chance of being shown
; on the whole, that means 90% target trials, 10% non-target-trials 
(defvar *target-trials* 20) ; number of target trials
(defvar *non-target-trials* 20) ; number of non-target trials

(defvar *output-directory* "~/output/") ; location where output files are stored
(defvar *trace-file-name* "sart-trace") ; name of file in which the trace is stored

(defvar *visible* nil) ; visibility of the experiment window

;; Global variables for data storage
(defvar *stimuli* nil)
(defvar *trial-response* nil)
(defvar *trial-start* nil)
(defvar *trial-rt* nil)
(defvar *trial-done* nil)
(defvar *all-responses* nil)
(defvar *all-rts* nil)

;; Do SART experiment n times, save trace to output file
(defun do-sart-n (n)
	(with-open-file 
		(*standard-output* 
			(ensure-directories-exist 
				(merge-pathnames 
					(make-pathname :name *trace-file-name* :type "txt")  
					*output-directory*
				)
            )
			:direction :output :if-does-not-exist :create :if-exists :supersede
		)
		
		(setf *visible* nil)
		(format t "Running ~a model participants~%" n)
		(dotimes (i n)
			(setf participant (1+ i))
			(format t "Run ~a...~%" participant)
			(do-sart)
			(write-results-to-file (concatenate 'string "dat" (write-to-string participant)) participant *stimuli* (reverse *all-responses*) (reverse *all-rts*))
		)
		(format t "Done~%")
	)
)

;; Do SART experiment 1 time
(defun do-sart ()
	(reset)
	(setf *all-responses* nil)
	(setf *all-rts* nil)
	(setf *stimuli* (permute-list (concatenate 'list (make-array *target-trials* :initial-element "1") (make-array *target-trials* :initial-element "2") (make-array *target-trials* :initial-element "4")
	(make-array *target-trials* :initial-element "5") (make-array *target-trials* :initial-element "6") (make-array *target-trials* :initial-element "7") (make-array *target-trials* :initial-element "8")
	(make-array *target-trials* :initial-element "9") (make-array *non-target-trials* :initial-element "3"))))
	(setf *visible* nil)
	(loop for stim in *stimuli* do (run-trial stim))
)

;; Do a single SART trial with a target stimulus
(defun do-sart-trial-1 ()
	(setf *visible* t)
	(run-trial "1")
)

(defun do-sart-trial-2 ()
	(setf *visible* t)
	(run-trial "2")
)
(defun do-sart-trial-4 ()
	(setf *visible* t)
	(run-trial "4")
)
(defun do-sart-trial-5 ()
	(setf *visible* t)
	(run-trial "5")
)

(defun do-sart-trial-6 ()
	(setf *visible* t)
	(run-trial "6")
)

(defun do-sart-trial-7 ()
	(setf *visible* t)
	(run-trial "7")
)

(defun do-sart-trial-8 ()
	(setf *visible* t)
	(run-trial "8")
)

(defun do-sart-trial-9 ()
	(setf *visible* t)
	(run-trial "9")
)

;; Do a single SART trial with a non-target stimulus
(defun do-sart-trial-3 ()
	(setf *visible* t)
	(run-trial "3")
)


;; Execute a trial with a given stimulus
(defun run-trial (stim)
  (let ((window (open-exp-window "SART Experiment"
                                 :visible *visible*
                                 :width 300
                                 :height 300
                                 :x 300
                                 :y 300))
        )
    
    (add-text-to-exp-window :text stim
                              :width 30
							  :height 30
                              :x 145
                              :y 150)
    
    (setf *trial-response* nil)
    (setf *trial-start* (get-time))
    (setf *trial-rt* nil)
	(setf *trial-done* nil)
	
	(install-device window)
	(proc-display)
	(run-full-time *stimulus-duration* :real-time *visible*)
	(allow-event-manager window)
	(clear-exp-window)
	(proc-display)
	(run-full-time *inter-stimulus-interval* :real-time *visible*))
	
	(push *trial-response* *all-responses*)
	(push *trial-rt* *all-rts*)
)

;; Register the model's key presses
(defmethod rpm-window-key-event-handler ((win rpm-window) key)
  (setf *trial-rt* (/ (- (get-time) *trial-start*) 1000.0))
  (setf *trial-response* (string key))
  (setf *trial-done* t)
)

;; Write the behavioural results to a file
(defun write-results-to-file (name participant stimuli responses rts)
	(with-open-file
		(out
			(ensure-directories-exist
				(merge-pathnames
					(make-pathname :name name :type "csv")
					*output-directory*
				)
			)
			:direction :output :if-does-not-exist :create :if-exists :supersede
		)
		(format out "participant, trial, stimulus, response, rt~%")
		(loop 
			for trial from 1
			for stimulus in stimuli
			for response in responses
			for rt in rts
			do (format out "~a, ~a, ~a, ~a, ~a~%" participant trial stimulus response rt)
		)
	)	
)


	
;;===================;;
;;    Model code     ;;
;;===================;;

(clear-all)

(define-model sart

;; Model parameters
(sgp :v t ; main trace detail
	:act low ; activation trace detail
	:sact t ; include activation trace in main trace

	:show-focus t ; show where the model is looking
	:esc t ; enable sub-symbolic level
	:rt -5 ; retrieval threshold
	:bll 0.5 ; base-level learning
	:ans 0.2 ;activation noise
)

(chunk-type beginning label)
(chunk-type goal state)
(chunk-type subgoal step goal)	; added goal to chunk-type to signify of which overarching goal this is a subgoal
(chunk-type srmapping stimulus hand)

(add-dm
	(start isa chunk)
	; responses to numbers
	(press-on-1 isa srmapping stimulus "1" hand left)
	(press-on-2 isa srmapping stimulus "2" hand left)
	(press-on-4 isa srmapping stimulus "4" hand left)
	(press-on-5 isa srmapping stimulus "5" hand left)
	(press-on-6 isa srmapping stimulus "6" hand left)
	(press-on-7 isa srmapping stimulus "7" hand left)
	(press-on-8 isa srmapping stimulus "8" hand left)
	(press-on-9 isa srmapping stimulus "9" hand left)
	(withhold-on-3 isa srmapping stimulus "3" hand nil)
	(startgoal isa beginning label start)
	(attend isa goal state attend)
	(wander isa goal state wander)
	(identify isa subgoal step identify goal attend)
	(get-response isa subgoal step get-response goal attend)
	(make-response isa subgoal step make-response goal attend)
	; from Mooney ways people can mind-wander
	(creative-thinking isa subgoal step creative-thinking goal wander)
	(future-planning isa subgoal step future-planning goal wander)
	(attentional-cycling isa subgoal step attentional-cycling goal wander)
	(dishabituation isa subgoal step dishabituation goal wander)
	(boredom-relief	isa	subgoal step boredom-relief goal wander)
	; overarching goal is wander because refocus must be called from wander state
	(refocus isa subgoal step refocus goal wander)
)

(set-base-levels
	(attend					10000	-10000)
	(wander					10000	-10000)
	(press-on-1				10000	-10000)
	(press-on-2				10000	-10000)
	(press-on-4				10000	-10000)
	(press-on-5				10000	-10000)
	(press-on-6				10000	-10000)
	(press-on-7				10000	-10000)
	(press-on-8				10000	-10000)
	(press-on-9				10000	-10000)
	(withhold-on-3			10000	-10000)
	; base-level for all subgoals is the same, so that all have the same chance of being chosen
	(creative-thinking 		10000 	-10000)
	(future-planning 		10000 	-10000)
	(attentional-cycling	10000	-10000)
	(dishabituation			10000	-10000)
	(boredom-relief			10000	-10000)
	(refocus				10000	-10000)
)

(p start-task
	=goal>
		isa			beginning
		label		start
	?retrieval>
		buffer		empty
		state		free
	-	state		error
==>
	+retrieval>
		isa			goal
		state		attend
	-goal>
)

(p check-current-goal
	=retrieval>
		isa			goal
		state		attend
	?retrieval>
		state		free
	-	state		error
	?goal>
		buffer 		empty
	?visual>
	-	scene-change T 	;there's nothing new to see on the screen
==>
	=retrieval>
		state		nil ;clear retrieval buffer without strengthening chunk
	-retrieval> 		;first nil it, then remove it
	+retrieval>
		isa			goal
	-	state		nil
	;we have to have an explicit goal, not just nil
)

(p start-mind-wandering
	=retrieval>
		isa			goal
		state		wander
	?goal>
		buffer 		empty
==>
	+goal>				
		isa 		goal
		state		wander
	=retrieval>
		state		nil ; clear retrieval buffer without strengthening chunk
	-retrieval>
	; get a first mind-wandering subgoal
	+retrieval>
		isa			subgoal
		goal		wander
)

(p continue-mind-wandering
	=goal>
		isa			goal
		state		wander
	; continue as long as refocus chunk is not retrieved
	=retrieval>
		isa 		subgoal
	-	step		refocus
==>
	=retrieval>
		state		nil 	; clear retrieval buffer without strengthening chunk
	-retrieval>
	; keep retrieving mind-wander chunks
	+retrieval>
		isa			subgoal
		goal		wander
	;we have to have an explicit goal, not just nil
)

(p stop-mind-wandering
	=goal>
		isa			goal
		state		wander
	=retrieval>
		isa			subgoal
		step		refocus
==>
	-goal>
	=retrieval>
		state		nil ;clear retrieval buffer without strengthening chunk
	-retrieval> 		;first nil it, then remove it
	; return to attend goal
	+retrieval>
		isa			goal
		state		attend
)

(p identify-stimulus
	?goal>
		buffer		empty
	=retrieval>
		isa			goal
		state		attend
	=visual-location>
	?visual>
		state		free
==>
	+visual>
		isa			move-attention
		screen-pos	=visual-location
	+goal>
		isa			subgoal
		step		get-response
	=retrieval>
		state		nil ; clear retrieval buffer without strengthening chunk
	-retrieval>
)

(p retrieve-response
	=goal>
		isa			subgoal
		step		get-response
	=visual>
		isa			text
		value		=number
	?visual>
		state		free
	?retrieval>
		state		free
==>
	+retrieval>
		isa			srmapping
		stimulus	=number
	+goal>
		isa			subgoal
		step		make-response
	+visual>
		isa			clear-scene-change
)

(p respond-if-not-3
	=goal>
		isa			subgoal
		step		make-response
	=retrieval>
		isa			srmapping
		stimulus	=number
		hand		=hand
	?manual>
		state		free
==>
	+manual>
		isa			punch
		hand		=hand
		finger		index
	-goal>
	-visual-location>
	-visual>
	+retrieval>
		isa			goal
	-	state		nil
)

(p do-not-respond-if-3
	=goal>
		isa			subgoal
		step		make-response
	=retrieval>
		isa			srmapping
		stimulus	=number
		hand		nil
==>
	-goal>
	-visual-location>
	-visual>
	+retrieval>
		isa			goal
	-	state		nil
)

(goal-focus startgoal)

)
