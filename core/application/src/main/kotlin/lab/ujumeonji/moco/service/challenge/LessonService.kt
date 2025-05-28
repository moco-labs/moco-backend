package lab.ujumeonji.moco.service.challenge

import lab.ujumeonji.moco.adapter.LessonRepositoryAdapter
import lab.ujumeonji.moco.model.CodeExercise
import lab.ujumeonji.moco.model.CodeStep
import lab.ujumeonji.moco.model.Lesson
import lab.ujumeonji.moco.model.LessonSection
import lab.ujumeonji.moco.model.Quiz
import lab.ujumeonji.moco.model.QuizOption
import lab.ujumeonji.moco.model.SectionType
import lab.ujumeonji.moco.model.Token
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LessonService(
    private val lessonRepositoryAdapter: LessonRepositoryAdapter,
    private val challengeService: ChallengeService,
) {
    private val logger = LoggerFactory.getLogger(LessonService::class.java)

    fun findAll(): List<Lesson> = lessonRepositoryAdapter.findAll()

    fun findById(id: String): Lesson? = lessonRepositoryAdapter.findById(id).orElse(null)

    fun findByChallengeId(challengeId: String): List<Lesson> = lessonRepositoryAdapter.findByChallengeId(challengeId)

    fun findOneByChallengeId(challengeId: String): Lesson? {
        val lessons = lessonRepositoryAdapter.findByChallengeId(challengeId)
        return lessons.firstOrNull()
    }

    fun findBySectionType(type: SectionType): List<Lesson> = lessonRepositoryAdapter.findBySectionsType(type)

    fun findByChallengeIdAndSectionType(
        challengeId: String,
        type: SectionType,
    ): List<Lesson> = lessonRepositoryAdapter.findByChallengeIdAndSectionsType(challengeId, type)

    fun save(lesson: Lesson): Lesson {
        val challenge = challengeService.findById(lesson.challengeId)
        if (challenge == null) {
            throw IllegalArgumentException("Challenge with ID ${lesson.challengeId} does not exist")
        }

        logger.info("레슨 저장 중: 챌린지 ID = {}", lesson.challengeId)
        return lessonRepositoryAdapter.save(lesson)
    }

    fun update(
        id: String,
        updatedLesson: Lesson,
    ): Lesson? {
        val existingLesson = lessonRepositoryAdapter.findById(id).orElse(null)

        return if (existingLesson != null) {
            val lesson =
                Lesson(
                    id = existingLesson.id,
                    challengeId = existingLesson.challengeId,
                    sections = updatedLesson.sections,
                    createdAt = existingLesson.createdAt,
                    updatedAt = LocalDateTime.now(),
                )
            lessonRepositoryAdapter.save(lesson)
        } else {
            null
        }
    }

    fun updateSections(
        id: String,
        sections: List<LessonSection>,
    ): Lesson? {
        val lesson = lessonRepositoryAdapter.findById(id).orElse(null)

        return if (lesson != null) {
            val updatedLesson =
                Lesson(
                    id = lesson.id,
                    challengeId = lesson.challengeId,
                    sections = sections,
                    createdAt = lesson.createdAt,
                    updatedAt = LocalDateTime.now(),
                )
            lessonRepositoryAdapter.save(updatedLesson)
        } else {
            null
        }
    }

    fun deleteById(id: String) {
        lessonRepositoryAdapter.deleteById(id)
    }

    fun deleteByChallengeId(challengeId: String) {
        val lessons = lessonRepositoryAdapter.findByChallengeId(challengeId)
        lessonRepositoryAdapter.deleteAll(lessons)
    }

    fun createBinarySearchLesson(challengeId: String): Lesson {
        val challenge = challengeService.findById(challengeId)
        if (challenge == null) {
            throw IllegalArgumentException("Challenge with ID $challengeId does not exist")
        }

        val binarySearchLesson =
            Lesson(
                challengeId = challengeId,
                sections =
                    listOf(
                        LessonSection(
                            title = "Introduction to Binary Search",
                            type = SectionType.TEXT,
                            data =
                                """
                                Binary search is an efficient algorithm for finding an item from a sorted list of items.
                                It works by repeatedly dividing in half the portion of the list that could contain the item,
                                until you've narrowed down the possible locations to just one.
                                """.trimIndent(),
                        ),
                        LessonSection(
                            title = "Quiz: Understanding Binary Search",
                            type = SectionType.GAP_FILL,
                            data =
                                Quiz(
                                    question =
                                        "Binary search requires the input array to be ___ " +
                                            "and has a time complexity of ___.",
                                    displayTokens =
                                        listOf(
                                            Token("Binary search requires the input array to be ", false),
                                            Token("sorted", true),
                                            Token(" and has a time complexity of ", false),
                                            Token("O(log n)", true),
                                            Token(".", false),
                                        ),
                                    choices =
                                        listOf(
                                            QuizOption("sorted", true),
                                            QuizOption("unsorted", false),
                                            QuizOption("reversed", false),
                                            QuizOption("random", false),
                                        ),
                                    correctOptionIndex = 0,
                                ),
                        ),
                        LessonSection(
                            title = "Binary Search Implementation",
                            type = SectionType.IMPLEMENTATION,
                            data =
                                CodeExercise(
                                    title = "Binary Search Implementation",
                                    description =
                                        "Arrange the code in the correct sequence to implement a binary search " +
                                            "algorithm that finds a target in a sorted array.",
                                    codeSteps =
                                        listOf(
                                            CodeStep(
                                                order = 1,
                                                code = """function binarySearch(arr, target) {""",
                                                explanation = "Define the function with array and target parameters",
                                            ),
                                            CodeStep(
                                                order = 2,
                                                code = """  let left = 0;""",
                                                explanation = "Initialize left pointer",
                                            ),
                                            CodeStep(
                                                order = 3,
                                                code = """  let right = arr.length - 1;""",
                                                explanation = "Initialize the right pointer to end of array",
                                            ),
                                            CodeStep(
                                                order = 4,
                                                code = """  while (left <= right) {""",
                                                explanation = "Continue while search space is valid",
                                            ),
                                            CodeStep(
                                                order = 5,
                                                code = """    let mid = Math.floor((left + right) / 2);""",
                                                explanation = "Calculate middle index",
                                            ),
                                            CodeStep(
                                                order = 6,
                                                code = """    if (arr[mid] === target) {""",
                                                explanation = "Check if middle element is the target",
                                            ),
                                            CodeStep(
                                                order = 7,
                                                code = """      return mid;""",
                                                explanation = "Return the index if found",
                                            ),
                                            CodeStep(
                                                order = 8,
                                                code = """    }""",
                                                explanation = "End the if condition",
                                            ),
                                            CodeStep(
                                                order = 9,
                                                code = """    else if (arr[mid] < target) {""",
                                                explanation = "If middle is less than target, search right half",
                                            ),
                                            CodeStep(
                                                order = 10,
                                                code = """      left = mid + 1;""",
                                                explanation = "Move left pointer to right of middle",
                                            ),
                                            CodeStep(
                                                order = 11,
                                                code = """    }""",
                                                explanation = "End the else if condition",
                                            ),
                                            CodeStep(
                                                order = 12,
                                                code = """    else {""",
                                                explanation = "If middle is greater than target, search left half",
                                            ),
                                            CodeStep(
                                                order = 13,
                                                code = """      right = mid - 1;""",
                                                explanation = "Move right pointer to left of middle",
                                            ),
                                            CodeStep(
                                                order = 14,
                                                code = """    }""",
                                                explanation = "End the else condition",
                                            ),
                                            CodeStep(
                                                order = 15,
                                                code = """  }""",
                                                explanation = "End the while loop",
                                            ),
                                            CodeStep(
                                                order = 16,
                                                code = """  return -1;""",
                                                explanation = "Return -1 if the target is not found",
                                            ),
                                            CodeStep(
                                                order = 17,
                                                code = """}""",
                                                explanation = "End the function",
                                            ),
                                        ),
                                ),
                        ),
                    ),
            )

        logger.info("이진 검색 레슨 생성 중: 챌린지 ID = {}", challengeId)
        return lessonRepositoryAdapter.save(binarySearchLesson)
    }

    fun createTreeTraversalLesson(challengeId: String): Lesson {
        val challenge = challengeService.findById(challengeId)
        if (challenge == null) {
            throw IllegalArgumentException("Challenge with ID $challengeId does not exist")
        }

        val treeTraversalLesson =
            Lesson(
                challengeId = challengeId,
                sections =
                    listOf(
                        LessonSection(
                            title = "Tree Traversal Methods",
                            type = SectionType.TEXT,
                            data =
                                """
                                Tree traversal is a fundamental operation in computer science that involves visiting
                                each node in a tree data structure exactly once. There are several methods to traverse
                                a tree, each with its own specific use cases:

                                1. **In-order Traversal**: Visit left subtree, current node, then right subtree
                                2. **Pre-order Traversal**: Visit current node, left subtree, then right subtree
                                3. **Post-order Traversal**: Visit left subtree, right subtree, then current node
                                4. **Level-order Traversal**: Visit nodes level by level from top to bottom

                                For binary search trees, in-order traversal gives us the elements in sorted order.
                                """.trimIndent(),
                        ),
                        LessonSection(
                            title = "Implementation Exercise",
                            type = SectionType.IMPLEMENTATION,
                            data =
                                CodeExercise(
                                    title = "In-order Tree Traversal",
                                    description =
                                        "Implement an in-order traversal function for a binary tree. " +
                                            "Arrange the code blocks in the correct order.",
                                    codeSteps =
                                        listOf(
                                            CodeStep(
                                                order = 1,
                                                code = """function inorderTraversal(root) {""",
                                                explanation = "Define the traversal function",
                                            ),
                                            CodeStep(
                                                order = 2,
                                                code = """  if (root === null) {""",
                                                explanation = "Base case: check if current node is null",
                                            ),
                                            CodeStep(
                                                order = 3,
                                                code = """    return;""",
                                                explanation = "Return early if node is null",
                                            ),
                                            CodeStep(
                                                order = 4,
                                                code = """  }""",
                                                explanation = "End the if condition",
                                            ),
                                            CodeStep(
                                                order = 5,
                                                code = """  inorderTraversal(root.left);""",
                                                explanation = "Recursively traverse left subtree first",
                                            ),
                                            CodeStep(
                                                order = 6,
                                                code = """  console.log(root.val);""",
                                                explanation = "Process current node (print its value)",
                                            ),
                                            CodeStep(
                                                order = 7,
                                                code = """  inorderTraversal(root.right);""",
                                                explanation = "Finally traverse right subtree",
                                            ),
                                            CodeStep(
                                                order = 8,
                                                code = """}""",
                                                explanation = "End the function",
                                            ),
                                        ),
                                ),
                        ),
                        LessonSection(
                            title = "Knowledge Check",
                            type = SectionType.GAP_FILL,
                            data =
                                Quiz(
                                    question =
                                        "In a binary search tree, which traversal method " +
                                            "visits nodes in ascending order?",
                                    displayTokens =
                                        listOf(
                                            Token("In a binary search tree, ", false),
                                            Token("in-order", true),
                                            Token(" traversal visits nodes in ascending order.", false),
                                        ),
                                    choices =
                                        listOf(
                                            QuizOption("in-order", true),
                                            QuizOption("pre-order", false),
                                            QuizOption("post-order", false),
                                            QuizOption("level-order", false),
                                        ),
                                    correctOptionIndex = 0,
                                ),
                        ),
                    ),
            )

        logger.info("트리 순회 레슨 생성 중: 챌린지 ID = {}", challengeId)
        return lessonRepositoryAdapter.save(treeTraversalLesson)
    }
}
