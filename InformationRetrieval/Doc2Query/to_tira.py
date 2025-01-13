# ------------------------------------ SETUP ------------------------------------------------

from tira.third_party_integrations import ensure_pyterrier_is_loaded
from tira.rest_api_client import Client

ensure_pyterrier_is_loaded()
tira = Client()

from pyterrier import get_dataset
import pickle

# ------------------------------------ CODE ------------------------------------------------

# dataset = "irds:ir-lab-wise-2024/subsampled-ms-marco-deep-learning-20241201-training"
# attempt_name = "bm25-doc2query-subsampled-ms-marco-deep-learning-20241201-training-inforet2024_25"

dataset = "irds:ir-lab-wise-2024/subsampled-ms-marco-rag-20250105-training"
attempt_name = "bm25-doc2query-subsampled-ms-marco-rag-20250105-training-inforet2024_25"

# dataset = "irds:ir-lab-wise-2024/subsampled-ms-marco-ir-lab-20250105-test"
# attempt_name = "bm25-doc2query-subsampled-ms-marco-ir-lab-20250105-test-inforet2024_25"

pt_dataset = get_dataset(dataset)

# load the 'run' object from file
with open(f"/content/data/{attempt_name}.pkl", "rb") as file:
    run = pickle.load(file)

persist_and_normalize_run(
    run,
    # Give your approach a short but descriptive name tag.
    system_name=attempt_name,
    default_output='/content/data/runs',
    upload_to_tira=pt_dataset,
)