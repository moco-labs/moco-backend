package lab.ujumeonji.chatalgoapi.dto

/**
 * DTO for challenge chat requests.
 *
 * @property userId The ID of the user making the request
 * @property message The message content from the user
 * @property sessionId Optional session ID, if continuing an existing chat session
 */
data class ChallengeChatRequest(
    val message: String,
)
