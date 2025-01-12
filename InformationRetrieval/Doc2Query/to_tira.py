"""# Upload to Tira and download the index and run"""

from tira.third_party_integrations import persist_and_normalize_run

persist_and_normalize_run(
    run,
    # Give your approach a short but descriptive name tag.
    system_name=attempt_name,
    default_output='/content/data/runs',
    upload_to_tira=pt_dataset,
)