package lab.ujumeonji.chatalgoapi.service

import lab.ujumeonji.chatalgoapi.model.CodeExercise
import lab.ujumeonji.chatalgoapi.model.CodeStep
import lab.ujumeonji.chatalgoapi.model.Lesson
import lab.ujumeonji.chatalgoapi.model.LessonSection
import lab.ujumeonji.chatalgoapi.model.Quiz
import lab.ujumeonji.chatalgoapi.model.QuizOption
import lab.ujumeonji.chatalgoapi.model.SectionType
import lab.ujumeonji.chatalgoapi.model.Token
import lab.ujumeonji.chatalgoapi.repository.ChallengeRepository
import lab.ujumeonji.chatalgoapi.repository.LessonRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LessonService(
    private val lessonRepository: LessonRepository,
    private val challengeRepository: ChallengeRepository,
) {
    private val logger = LoggerFactory.getLogger(LessonService::class.java)

    fun findAll(): List<Lesson> = lessonRepository.findAll()

    fun findById(id: String): Lesson? = lessonRepository.findById(id).orElse(null)

    fun findByChallengeId(challengeId: String): List<Lesson> = lessonRepository.findByChallengeId(challengeId)

    fun findOneByChallengeId(challengeId: String): Lesson? {
        val lessons = lessonRepository.findByChallengeId(challengeId)
        return lessons.firstOrNull()
    }

    fun findBySectionType(type: SectionType): List<Lesson> = lessonRepository.findBySectionsType(type)

    fun findByChallengeIdAndSectionType(
        challengeId: String,
        type: SectionType,
    ): List<Lesson> = lessonRepository.findByChallengeIdAndSectionsType(challengeId, type)

    fun save(lesson: Lesson): Lesson {
        val challenge = challengeRepository.findById(lesson.challengeId)
        if (challenge.isEmpty) {
            throw IllegalArgumentException("Challenge with ID ${lesson.challengeId} does not exist")
        }

        logger.info("레슨 저장 중: 챌린지 ID = {}", lesson.challengeId)
        return lessonRepository.save(lesson)
    }

    fun update(
        id: String,
        updatedLesson: Lesson,
    ): Lesson? {
        val existingLesson = lessonRepository.findById(id).orElse(null)

        return if (existingLesson != null) {
            val lesson =
                updatedLesson.copy(
                    id = existingLesson.id,
                    challengeId = existingLesson.challengeId,
                    createdAt = existingLesson.createdAt,
                    updatedAt = LocalDateTime.now(),
                )
            lessonRepository.save(lesson)
        } else {
            null
        }
    }

    fun updateSections(
        id: String,
        sections: List<LessonSection>,
    ): Lesson? {
        val lesson = lessonRepository.findById(id).orElse(null)

        return if (lesson != null) {
            val updatedLesson =
                lesson.copy(
                    sections = sections,
                    updatedAt = LocalDateTime.now(),
                )
            lessonRepository.save(updatedLesson)
        } else {
            null
        }
    }

    fun deleteById(id: String) {
        lessonRepository.deleteById(id)
    }

    fun deleteByChallengeId(challengeId: String) {
        val lessons = lessonRepository.findByChallengeId(challengeId)
        lessonRepository.deleteAll(lessons)
    }

    fun createBinarySearchLesson(challengeId: String): Lesson {
        val challenge = challengeRepository.findById(challengeId)
        if (challenge.isEmpty) {
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
                            title = "Binary Search Knowledge Check",
                            type = SectionType.GAP_FILL,
                            data =
                                Quiz(
                                    question = "The time complexity of binary search is ________.",
                                    displayTokens =
                                        listOf(
                                            Token("The time complexity of binary search is ", false),
                                            Token("O(log n)", true),
                                            Token(".", false),
                                        ),
                                    choices =
                                        listOf(
                                            QuizOption("O(n)", false),
                                            QuizOption("O(log n)", true),
                                            QuizOption("O(n log n)", false),
                                            QuizOption("O(1)", false),
                                        ),
                                    correctOptionIndex = 1,
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
                                                explanation = "Continue searching as long as the search range is valid",
                                            ),
                                            CodeStep(
                                                order = 5,
                                                code = """    const mid = Math.floor((left + right) / 2);""",
                                                explanation = "Calculate the middle index of the current search range",
                                            ),
                                            CodeStep(
                                                order = 6,
                                                code = """    if (arr[mid] === target) {""",
                                                explanation = "Check if the middle element is the target",
                                            ),
                                            CodeStep(
                                                order = 7,
                                                code = """      return mid;""",
                                                explanation = "Return the index if the target is found",
                                            ),
                                            CodeStep(
                                                order = 8,
                                                code = """    }""",
                                                explanation = "End the condition for target found",
                                            ),
                                            CodeStep(
                                                order = 9,
                                                code = """    if (arr[mid] < target) {""",
                                                explanation = "Check if the target is in the right half",
                                            ),
                                            CodeStep(
                                                order = 10,
                                                code = """      left = mid + 1;""",
                                                explanation = "Move the left pointer to search the right half",
                                            ),
                                            CodeStep(
                                                order = 11,
                                                code = """    } else {""",
                                                explanation = "Otherwise, the target is in the left half",
                                            ),
                                            CodeStep(
                                                order = 12,
                                                code = """      right = mid - 1;""",
                                                explanation = "Move the right pointer to exclude the right half",
                                            ),
                                            CodeStep(
                                                order = 13,
                                                code = """    }""",
                                                explanation = "End the condition for search space adjustment",
                                            ),
                                            CodeStep(
                                                order = 14,
                                                code = """  }""",
                                                explanation = "End the while loop",
                                            ),
                                            CodeStep(
                                                order = 15,
                                                code = """  return -1;""",
                                                explanation = "Return -1 if the target is not found",
                                            ),
                                            CodeStep(
                                                order = 16,
                                                code = """}""",
                                                explanation = "End the function",
                                            ),
                                        ),
                                ),
                        ),
                    ),
            )

        logger.info("이진 검색 레슨 생성 중: 챌린지 ID = {}", challengeId)
        return lessonRepository.save(binarySearchLesson)
    }

    fun createTreeTraversalLesson(challengeId: String): Lesson {
        val challenge = challengeRepository.findById(challengeId)
        if (challenge.isEmpty) {
            throw IllegalArgumentException("Challenge with ID $challengeId does not exist")
        }

        val treeTraversalLesson =
            Lesson(
                challengeId = challengeId,
                sections =
                    listOf(
                        LessonSection(
                            title = "Tree Traversal Techniques",
                            type = SectionType.TEXT,
                            data =
                                """
                                Tree traversal is a form of graph traversal and refers to the process of visiting
                                each node in a tree data structure, exactly once. There are three common ways to
                                traverse a tree in depth-first order: in-order, pre-order, and post-order.

                                - In-order traversal: Visit left subtree, then root, then right subtree.
                                - Pre-order traversal: Visit root, then left subtree, then right subtree.
                                - Post-order traversal: Visit left subtree, then right subtree, then root.
                                """.trimIndent(),
                        ),
                        LessonSection(
                            title = "Tree Traversal Implementation",
                            type = SectionType.IMPLEMENTATION,
                            data =
                                CodeExercise(
                                    title = "In-order Traversal Implementation",
                                    description = "Implement an in-order traversal algorithm for a binary tree.",
                                    codeSteps =
                                        listOf(
                                            CodeStep(
                                                order = 1,
                                                code = """function inorderTraversal(root) {""",
                                                explanation = "Define the in-order traversal function",
                                            ),
                                            CodeStep(
                                                order = 2,
                                                code = """  const result = [];""",
                                                explanation = "Initialize an array to store the traversal result",
                                            ),
                                            CodeStep(
                                                order = 3,
                                                code = """  function traverse(node) {""",
                                                explanation = "Define a helper function for recursion",
                                            ),
                                            CodeStep(
                                                order = 4,
                                                code = """    if (node === null) return;""",
                                                explanation = "Base case: if the node is null, return",
                                            ),
                                            CodeStep(
                                                order = 5,
                                                code = """    traverse(node.left);""",
                                                explanation = "Recursively traverse the left subtree first",
                                            ),
                                            CodeStep(
                                                order = 6,
                                                code = """    result.push(node.val);""",
                                                explanation = "Visit the current node (add its value to the result)",
                                            ),
                                            CodeStep(
                                                order = 7,
                                                code = """    traverse(node.right);""",
                                                explanation = "Recursively traverse the right subtree",
                                            ),
                                            CodeStep(
                                                order = 8,
                                                code = """  }""",
                                                explanation = "End of helper function",
                                            ),
                                            CodeStep(
                                                order = 9,
                                                code = """  traverse(root);""",
                                                explanation = "Start the traversal from the root",
                                            ),
                                            CodeStep(
                                                order = 10,
                                                code = """  return result;""",
                                                explanation = "Return the traversal result",
                                            ),
                                            CodeStep(
                                                order = 11,
                                                code = """}""",
                                                explanation = "End of function",
                                            ),
                                        ),
                                ),
                        ),
                        LessonSection(
                            title = "Tree Traversal Knowledge Check",
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
        return lessonRepository.save(treeTraversalLesson)
    }
}
